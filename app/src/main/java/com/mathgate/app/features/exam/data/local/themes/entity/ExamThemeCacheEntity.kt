package com.mathgate.app.features.exam.data.local.themes.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathgate.app.features.exam.domain.entity.ExamTypes

@Entity(tableName = "exam_theme_cache")
data class ExamThemeCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val number: Int,
    val label: String,
    val tasks: Int,
    val cachedAt: Long = System.currentTimeMillis(),
    val type: ExamTypes
)
