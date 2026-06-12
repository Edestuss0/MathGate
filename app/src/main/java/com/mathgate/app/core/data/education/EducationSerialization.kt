package com.mathgate.app.core.data.education

import androidx.room.Embedded
import androidx.room.Relation
import com.mathgate.app.core.data.lesson_blocks.LessonBlockEntity
import com.mathgate.app.core.data.lesson_blocks.toDomain
import com.mathgate.app.core.data.lesson_pages.LessonPageEntity
import com.mathgate.app.core.data.lessons.LessonEntity
import com.mathgate.app.core.entities.Lesson
import com.mathgate.app.core.entities.LessonPage

data class LessonWithPages(
    @Embedded
    val lesson: LessonEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "lessonId"
    )
    val pages: List<LessonPageEntity>
)

fun LessonWithPages.toLesson(blocks: List<LessonBlockEntity>): Lesson {
    val blocksByPage = blocks.groupBy { it.pageId }
    return Lesson(
        id = lesson.id,
        title = lesson.title,
        description = lesson.description,
        themeId = lesson.themeId,
        pages = pages.sortedBy { it.orderIndex }.map { page ->
            LessonPage(
                id = page.id,
                orderIndex = page.orderIndex,
                blocks = (blocksByPage[page.id] ?: emptyList())
                    .sortedBy { it.orderIndex }
                    .map { it.toDomain() }
            )
        }
    )
}
