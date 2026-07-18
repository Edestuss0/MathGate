package com.mathgate.app.core.analytics.core

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.mathgate.app.core.analytics.entity.AnalyticsEvent
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.component1
import kotlin.collections.component2

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebase: FirebaseAnalytics
) {
    fun log(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            event.params.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value.take(100))
                    is Long -> putLong(key, value)
                    is Int -> putLong(key, value.toLong())
                    is Double -> putDouble(key, value)
                    is Boolean -> putString(key, value.toString())
                }
            }
        }
        firebase.logEvent(event.name, bundle)
    }

    fun setUserId(id: String?) {
        firebase.setUserId(id)
    }

    fun setUserUsername(username: String?) {
        firebase.setUserProperty("username", username)
    }
}