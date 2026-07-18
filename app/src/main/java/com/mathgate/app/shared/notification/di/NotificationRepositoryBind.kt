package com.mathgate.app.shared.notification.di

import com.mathgate.app.shared.notification.data.repository.NotificationRepositoryImpl
import com.mathgate.app.shared.notification.domain.repository.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationRepositoryBind {
    @Binds @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}