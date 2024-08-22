package com.example.wework.data.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val weWorkAppDispatchers: WeWorkAppDispatchers)

enum class WeWorkAppDispatchers {
    IO
}