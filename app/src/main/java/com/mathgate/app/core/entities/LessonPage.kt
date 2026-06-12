package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class LessonPage(
    val id: Int,
    val orderIndex: Int,
    val blocks: List<LessonBlock>
)

@Serializable
data class LessonPageDto(
    val id: Int,
    val orderIndex: Int,
    val blocks: List<LessonBlockDto>
)
