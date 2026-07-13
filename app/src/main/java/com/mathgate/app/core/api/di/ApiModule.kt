package com.mathgate.app.core.api.di

import com.mathgate.app.core.api.core.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideApiClient(): ApiClient = ApiClient
}