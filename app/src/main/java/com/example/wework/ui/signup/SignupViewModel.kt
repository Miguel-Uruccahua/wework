package com.example.wework.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wework.domain.signup.SignupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupRepository: SignupRepository
): ViewModel() {

    private var _user = MutableLiveData<String>("")
    val user: LiveData<String> = _user
    fun onChangeUser(value: String) { _user.value = value }

    private var _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password
    fun onChangePassword(value: String) { _password.value = value }


    fun register(navigateToHome:()->Unit, showError:(String)->Unit){
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO){
                    signupRepository.register(_user.value!!,_password.value!!)
                }
                if (result!=null){
                    navigateToHome()
                }
            }catch (e:Exception){
                showError(e.message.toString())
            }
        }
    }
}