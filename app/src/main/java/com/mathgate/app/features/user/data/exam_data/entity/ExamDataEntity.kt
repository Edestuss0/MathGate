package com.mathgate.app.features.user.data.exam_data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathgate.app.shared.exam.entity.ExamTypes

@Entity(tableName = "exam_data")
data class ExamDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val answer: String,
    val isCorrect: Boolean,
    val themeLabel: String,
    val themeNumber: Int,
    val type: ExamTypes,
    val date: String
)