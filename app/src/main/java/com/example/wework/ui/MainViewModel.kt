package com.example.wework.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wework.domain.account.AccountRepository
import com.example.wework.domain.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountRepository: AccountRepository
):ViewModel() {

    fun checkLogged(onLogged:(Boolean)->Unit) {
        onLogged(accountRepository.isUserLogged())
    }

    fun logout(){
        accountRepository.logout()
    }

    private var _userInfo: Flow<String> = accountRepository.getUser()
    val userInfo: Flow<String> = _userInfo

}