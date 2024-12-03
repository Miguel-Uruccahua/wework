package com.example.wework.ui.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wework.domain.analytics.models.AnalyticsModel
import com.example.wework.domain.analytics.AnalyticsRepository
import com.example.wework.domain.login.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val analyticsRepository: AnalyticsRepository,
) : ViewModel() {

    lateinit var verificationCode: String

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _user = MutableLiveData<String>("")
    val user: LiveData<String> = _user
    fun onChangeUser(value: String) { _user.value = value }

    private var _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password
    fun onChangePassword(value: String) { _password.value = value }

    private var _numberPhone = MutableLiveData<String>("")
    val numberPhone: LiveData<String> = _numberPhone
    fun onChangeNumberPhone(value: String) { _numberPhone.value = value }

    private var _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String> = _errorMessage


    fun loginWithPhone(
        phoneNumber: String,
        activity: Activity,
        onVerificationComplete: () -> Unit,
        onCodeSent: () -> Unit,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credentials: PhoneAuthCredential) {
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            authRepository.completeRegisterWithPhoneVerification(credentials)
                        }
                        if (result != null) {
                            onVerificationComplete()
                        }
                    }
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    _isLoading.value = false
                    _errorMessage.value = p0.message.orEmpty()
                }

                override fun onCodeSent(
                    verificationCode: String, p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@LoginViewModel.verificationCode = verificationCode
                    _isLoading.value = false
                    onCodeSent()
                }
            }
            withContext(Dispatchers.IO) {
                authRepository.loginWithPhone(phoneNumber, activity, callback)
            }
            _isLoading.value = false
        }
    }

    fun verifyCode(phoneCode: String, onSuccessVerification: () -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                authRepository.verifyCode(verificationCode, phoneCode)
            }
            if (result != null) {
                onSuccessVerification()
            }
        }
    }

    fun getGoogleClient(googleLauncherLogin: (GoogleSignInClient) -> Unit) {
        val gsc = authRepository.getGoogleClient()
        googleLauncherLogin(gsc)
    }

    fun loginWithGoogle(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val resul = withContext(Dispatchers.IO) {
                authRepository.loginWithGoogle(idToken)
            }
            if (resul != null) {
                onSuccess()
            }
        }
    }

    fun onLoginSelected(
        typeLogin:LoginType,
        activity: Activity,
        navigateToHome: () -> Unit,
        showError: (String) -> Unit
    ){
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                withContext(Dispatchers.IO){
                    when (typeLogin){
                        LoginType.Email -> authRepository.login(_user.value!!, _password.value!!)
                        LoginType.Github -> authRepository.loginWithGithub(activity)
                        LoginType.Microsoft -> authRepository.loginWithMicrosoft(activity)
                        LoginType.Anonymously -> authRepository.loginAnonymously()
                        LoginType.Twitter -> authRepository.loginWithTwitter(activity)
                    }
                }
            }.onSuccess {
                if (it != null) navigateToHome()
            }.onFailure {
                showError(it.message.toString())
            }
            _isLoading.value = false
        }
    }

    fun sendData(){
        val analyticsModel = AnalyticsModel(title = "Mititle", analyticsString = listOf(Pair("Ingreso","Account")))
        viewModelScope.launch{
            analyticsRepository.sendData(analyticsModel)
        }
    }

}



sealed class LoginType(){
    object Github: LoginType()
    object Microsoft: LoginType()
    object Email: LoginType()
    object Twitter: LoginType()
    object Anonymously:LoginType()
}