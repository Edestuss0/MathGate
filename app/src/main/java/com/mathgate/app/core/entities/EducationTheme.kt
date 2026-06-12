package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class EducationTheme(
    val id: Int,
    val name: String,
    val grade: Int
)
