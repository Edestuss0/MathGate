package com.mathgate.app.shared.notification.domain.repository

import com.mathgate.app.shared.notification.domain.entity.AppNotification

interface NotificationRepository {
    fun show(notification: AppNotification)
}