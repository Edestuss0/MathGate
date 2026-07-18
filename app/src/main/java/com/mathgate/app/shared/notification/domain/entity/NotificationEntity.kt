package com.mathgate.app.shared.notification.domain.entity

enum class NotificationChannelType(
    val channelId: String,
    val channelName: String,
    val importance: NotificationImportance
) {
    NEWS_UPDATES("news_updates", "Новости и обновления", NotificationImportance.DEFAULT),
    REMINDERS("reminders", "Напоминания", NotificationImportance.HIGH)
}

enum class NotificationImportance {
    DEFAULT, HIGH
}

data class AppNotification(
    val id: Int,
    val title: String,
    val message: String,
    val channel: NotificationChannelType,
    val deepLink: String? = null
)