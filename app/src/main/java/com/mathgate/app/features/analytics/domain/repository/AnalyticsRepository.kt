package com.mathgate.app.features.analytics.domain.repository

import com.mathgate.app.features.analytics.domain.entity.AnalyticsEvent

interface AnalyticsRepository {
    fun log(event: AnalyticsEvent)
}