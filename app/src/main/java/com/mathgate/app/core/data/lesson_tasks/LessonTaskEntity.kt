package com.mathgate.app.core.data.lesson_tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "lesson_tasks",
    primaryKeys = ["lessonId", "question"]
)
data class LessonTaskEntity(
    val lessonId: Int,
    val question: String,
    val answer: List<String>
)