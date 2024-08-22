package com.example.wework.data.repository

import android.app.Activity
import android.content.Context
import com.example.wework.R
import com.example.wework.domain.login.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context,
) : AuthRepository {

    private suspend fun completeRegisterWithCredential(credentials: AuthCredential): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithCredential(credentials).addOnSuccessListener {
                cancellableContinuation.resume(it.user)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    private suspend fun initRegisterWithProvider(
        activity: Activity, provider: OAuthProvider
    ): FirebaseUser? {
        return suspendCancellableCoroutine<FirebaseUser?> { cancellableContinuation ->
            firebaseAuth.pendingAuthResult?.addOnSuccessListener {
                cancellableContinuation.resume(it.user)
            }?.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            } ?: completeRegisterWithProvider(activity, provider, cancellableContinuation)
        }
    }

    private fun completeRegisterWithProvider(
        activity: Activity,
        provider: OAuthProvider,
        cancellableContinuation: CancellableContinuation<FirebaseUser?>
    ) {
        firebaseAuth.startActivityForSignInWithProvider(activity, provider).addOnSuccessListener {
            cancellableContinuation.resume(it.user)
        }.addOnFailureListener {
            cancellableContinuation.resumeWithException(it)
        }
    }

    override suspend fun login(user: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(user, password).await().user
    }

    override suspend fun loginWithPhone(
        phoneNumber: String,
        activity: Activity,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS).setCallbacks(callback).setActivity(activity).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun completeRegisterWithPhoneVerification(credentials: PhoneAuthCredential) =
        completeRegisterWithCredential(credentials)

    override suspend fun verifyCode(
        verificationCode: String, phoneCode: String
    ): FirebaseUser? {
        val credentials = PhoneAuthProvider.getCredential(verificationCode, phoneCode)
        return completeRegisterWithCredential(credentials)
    }

    override suspend fun loginWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return completeRegisterWithCredential(credential)
    }

    override fun getGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    override suspend fun loginWithGithub(activity: Activity): FirebaseUser? {
        val provider = OAuthProvider.newBuilder("github.com").apply {
            scopes = listOf("user:email")
        }.build()
        return initRegisterWithProvider(activity, provider)
    }

    override suspend fun loginWithMicrosoft(activity: Activity): FirebaseUser? {
        val provider = OAuthProvider.newBuilder("microsoft.com").apply {
            scopes = listOf("mail.read", "calendars.read")
        }.build()
        return initRegisterWithProvider(activity, provider)
    }

    override suspend fun loginAnonymously(): FirebaseUser? {
        return firebaseAuth.signInAnonymously().await().user
    }

    override suspend fun loginWithTwitter(activity: Activity): FirebaseUser? {
        val provider = OAuthProvider.newBuilder("twitter.com").build()
        return initRegisterWithProvider(activity, provider)
    }

}