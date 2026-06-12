package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val id: Int,
    val themeId: Int,
    val title: String,
    val description: String,
    val pages: List<LessonPage>
)

@Serializable
data class LessonDto(
    val id: Int,
    val themeId: Int,
    val title: String,
    val description: String,
    val pages: List<LessonPageDto>
) {
    fun toLesson(): Lesson {
        val pages = pages.map { page ->
            val blocks = page.blocks.map { block ->
                block.toBlock()
            }
            LessonPage(
                id = page.id,
                orderIndex = page.orderIndex,
                blocks = blocks
            )
        }
        return Lesson(
            id = id,
            themeId = themeId,
            title = title,
            description = description,
            pages = pages
        )
    }
}
