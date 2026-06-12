package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class DownloadedTheme(
    val id: Int,
    val name: String,
    val grade: Int,
    val lessons: List<LessonDto>
)
