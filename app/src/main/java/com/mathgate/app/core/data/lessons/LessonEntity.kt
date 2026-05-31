package com.mathgate.app.core.data.lessons

import androidx.room.Entity
import androidx.room.PrimaryKey

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