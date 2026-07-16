package com.mathgate.app.features.exam.data.local.questions.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathgate.app.shared.exam.entity.ExamTypes

@Entity(tableName = "exam_cache")
data class ExamQuestionCacheEntity(
    @PrimaryKey
    val id: Int,
    val json: String,
    val type: ExamTypes,
    val cachedAt: Long = System.currentTimeMillis(),
    val number: Int
)
