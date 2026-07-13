package com.mathgate.app.features.user.data.exam_data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_data")
data class ExamDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val answer: String,
    val isCorrect: Boolean,
)