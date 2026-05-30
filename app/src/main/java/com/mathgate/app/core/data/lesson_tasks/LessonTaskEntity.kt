package com.mathgate.app.core.data.lesson_tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_tasks")
data class LessonTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lessonId: Int,
    val question: String,
    val answer: List<String>
)