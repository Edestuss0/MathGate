package com.mathgate.app.core.data.lessons

import androidx.room.Entity

@Entity(
    tableName = "lessons",
    primaryKeys = ["id", "educationId"]
)
data class LessonEntity(
    val id: Int,
    val educationId: Int,
    val name: String,
    val material: String
)
