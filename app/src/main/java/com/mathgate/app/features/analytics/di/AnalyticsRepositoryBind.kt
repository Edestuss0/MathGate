package com.mathgate.app.features.analytics.di

import com.mathgate.app.features.analytics.data.repository.AnalyticsRepositoryImpl
import com.mathgate.app.features.analytics.domain.repository.AnalyticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsRepositoryBind {
    @Binds @Singleton
    abstract fun bindAnalyticsRepository(
        impl: AnalyticsRepositoryImpl
    ): AnalyticsRepository
}