package com.mathgate.app.features.analytics.data.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.mathgate.app.features.analytics.domain.entity.AnalyticsEvent
import com.mathgate.app.features.analytics.domain.repository.AnalyticsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val firebase: FirebaseAnalytics
) : AnalyticsRepository {
    override fun log(event: AnalyticsEvent) {
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
}