package com.mathgate.app.shared.notification.domain.usecases

import com.mathgate.app.shared.notification.domain.entity.AppNotification
import com.mathgate.app.shared.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class ShowNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(notification: AppNotification) {
        repository.show(notification)
    }
}