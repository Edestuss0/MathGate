package com.mathgate.app.core.data.lesson_pages

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mathgate.app.core.data.lessons.LessonEntity

@Entity(
    tableName = "lesson_pages",
    foreignKeys = [
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("lessonId")]
)
data class LessonPageEntity(
    @PrimaryKey
    val id: Int,
    val lessonId: Int,
    val orderIndex: Int
)
