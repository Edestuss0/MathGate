package com.mathgate.app.core.analytics.entity

interface AnalyticsEvent {
    val name: String
    val params: Map<String, Any>
}