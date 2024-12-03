package com.example.wework.data.repository

import com.example.wework.domain.analytics.models.AnalyticsModel
import com.example.wework.domain.analytics.AnalyticsRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val analytics:FirebaseAnalytics
):AnalyticsRepository {
    override suspend fun sendData(analyticsModel: AnalyticsModel) {
        analytics.logEvent(analyticsModel.title){
            analyticsModel.analyticsString.map { param(it.first, it.second) }
            analyticsModel.analyticsDouble.map { param(it.first, it.second) }
            analyticsModel.analyticsLong.map { param(it.first, it.second) }
            analyticsModel.analyticsBundle.map { param(it.first, it.second) }
            analyticsModel.analyticsBundleArray.map { param(it.first, it.second.toTypedArray()) }
        }
    }
}