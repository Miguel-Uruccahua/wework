package com.example.wework.domain.account

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun isUserLogged():Boolean
    fun logout()
    fun getUser(): Flow<String>
}