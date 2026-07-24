package com.mathgate.app.core.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(
        ioDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(
        SupervisorJob() + ioDispatcher +
                CoroutineExceptionHandler { _, throwable ->
                    Log.e("AppScope", "Unhandled coroutine exception", throwable)
                }
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope