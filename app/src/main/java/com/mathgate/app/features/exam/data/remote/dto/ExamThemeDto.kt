package com.mathgate.app.features.exam.data.remote.dto

import com.mathgate.app.features.exam.domain.entity.ExamTheme
import kotlinx.serialization.Serializable

@Serializable
data class ExamThemeDto(
    val number: Int,
    val label: String,
    val tasks: Int
)

fun ExamThemeDto.toDomain(): ExamTheme = ExamTheme(
    number = number,
    label = label,
    tasks = tasks
)
