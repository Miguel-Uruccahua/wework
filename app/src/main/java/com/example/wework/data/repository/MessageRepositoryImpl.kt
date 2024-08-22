package com.example.wework.data.repository

import com.example.wework.domain.home.MessageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val reference: DatabaseReference
): MessageRepository {


}