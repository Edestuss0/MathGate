package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class LessonByTheme(
    val title: String,
    val description: String,
    val orderIndex: Int,
    val id: Int,
)
