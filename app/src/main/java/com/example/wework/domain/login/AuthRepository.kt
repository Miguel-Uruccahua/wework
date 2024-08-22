package com.example.wework.domain.login

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

interface AuthRepository {

    suspend fun login(
        user: String,
        password: String,
    ): FirebaseUser?

    suspend fun loginWithPhone(
        phoneNumber: String,
        activity: Activity,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    suspend fun completeRegisterWithPhoneVerification(
        credentials: PhoneAuthCredential
    ): FirebaseUser?

    suspend fun verifyCode(
        verificationCode: String, phoneCode: String
    ): FirebaseUser?

    suspend fun loginWithGoogle(idToken: String): FirebaseUser?

    fun getGoogleClient(): GoogleSignInClient

    suspend fun loginWithGithub(activity: Activity): FirebaseUser?

    suspend fun loginWithMicrosoft(activity: Activity): FirebaseUser?

    suspend fun loginAnonymously(): FirebaseUser?

    suspend fun loginWithTwitter(activity: Activity): FirebaseUser?

}