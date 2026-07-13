package com.mathgate.app.features.analytics.domain.entity

interface AnalyticsEvent {
    val name: String
    val params: Map<String, Any>
}