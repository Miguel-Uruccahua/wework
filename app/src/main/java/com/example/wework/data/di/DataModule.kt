package com.example.wework.data.di

import com.example.wework.data.repository.AccountRepositoryImpl
import com.example.wework.data.repository.AnalyticsRepositoryImpl
import com.example.wework.data.repository.AuthRepositoryImpl
import com.example.wework.data.repository.MessageRepositoryImpl
import com.example.wework.data.repository.SignupRepositoryImpl
import com.example.wework.domain.account.AccountRepository
import com.example.wework.domain.analytics.AnalyticsRepository
import com.example.wework.domain.home.MessageRepository
import com.example.wework.domain.login.AuthRepository
import com.example.wework.domain.signup.SignupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Singleton
    @Binds
    fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ):AuthRepository

    @Singleton
    @Binds
    fun bindsMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Singleton
    @Binds
    fun bindsAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Singleton
    @Binds
    fun bindsSignUpRepository(
        signupRepositoryImpl: SignupRepositoryImpl
    ): SignupRepository

    @Singleton
    @Binds
    fun bindsAnalyticsRepository(
        analyticsRepositoryImpl: AnalyticsRepositoryImpl
    ): AnalyticsRepository

}