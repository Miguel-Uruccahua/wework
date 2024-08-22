package com.example.wework.domain.signup

import com.google.firebase.auth.FirebaseUser

interface SignupRepository {

    suspend fun register(
        user:String,
        password:String,
    ):FirebaseUser?

}