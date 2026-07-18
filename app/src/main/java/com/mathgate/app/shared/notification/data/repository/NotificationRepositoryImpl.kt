package com.mathgate.app.shared.notification.data.repository

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mathgate.app.MainActivity
import com.mathgate.app.R
import com.mathgate.app.shared.notification.domain.entity.AppNotification
import com.mathgate.app.shared.notification.domain.entity.NotificationChannelType
import com.mathgate.app.shared.notification.domain.entity.NotificationImportance
import com.mathgate.app.shared.notification.domain.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationRepository {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelType.entries.forEach { type ->
                val importance = when (type.importance) {
                    NotificationImportance.HIGH -> NotificationManager.IMPORTANCE_HIGH
                    NotificationImportance.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                }
                val channel = NotificationChannel(type.channelId, type.channelName, importance)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun show(notification: AppNotification) {
        val intent = if (!notification.deepLink.isNullOrBlank()) {
            Intent(Intent.ACTION_VIEW, Uri.parse(notification.deepLink).apply {
                MainActivity::class.java
            })
        } else {
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, notification.channel.channelId)
            .setSmallIcon(R.drawable.outline_school_24)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(getCompatPriority(notification.channel.importance))

        notificationManager.notify(notification.id, builder.build())

    }

    private fun getCompatPriority(importance: NotificationImportance): Int {
        return when (importance) {
            NotificationImportance.HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationImportance.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
        }
    }

}