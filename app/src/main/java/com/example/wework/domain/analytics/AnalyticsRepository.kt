package com.example.wework.domain.analytics

import com.example.wework.domain.analytics.models.AnalyticsModel
import com.google.firebase.auth.FirebaseUser

interface AnalyticsRepository {

    suspend fun sendData(analyticsModel: AnalyticsModel)

}