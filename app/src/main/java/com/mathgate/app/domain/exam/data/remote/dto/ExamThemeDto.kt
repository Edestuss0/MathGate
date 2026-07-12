package com.mathgate.app.domain.exam.data.remote.dto

import com.mathgate.app.domain.exam.entity.ExamTheme
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
