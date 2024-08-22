package com.example.wework.data.repository

import com.example.wework.data.di.Dispatcher
import com.example.wework.data.di.WeWorkAppDispatchers
import com.example.wework.domain.account.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Dispatcher(WeWorkAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : AccountRepository {

    private fun getCurrentUser() = firebaseAuth.currentUser
    override fun isUserLogged() = getCurrentUser() != null
    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getUser() = flow {
        val data = getCurrentUser()
        if (data == null) {
            emit("Ocurrió un error!")
        }else{
            emit(data.displayName ?: if (data.isAnonymous) "Anónimo" else "Ocurrió un error!")
        }
    }.onCompletion {}.flowOn(ioDispatcher)

}