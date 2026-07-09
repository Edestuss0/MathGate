package com.mathgate.app.data.features.exam.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathgate.app.domain.exam.entity.ExamTypes

@Entity(tableName = "exam_cache")
data class ExamCacheEntity(
    @PrimaryKey
    val id: Int,
    val json: String,
    val type: ExamTypes,
    val cachedAt: Long = System.currentTimeMillis()
)
