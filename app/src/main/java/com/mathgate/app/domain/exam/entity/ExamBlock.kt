package com.mathgate.app.domain.exam.entity

import kotlinx.serialization.Serializable

@Serializable
data class ExamBlock (
    val type: ExamBlockType,
    val content: String,
)

enum class ExamBlockType {
    TEXT, IMAGE, FORMULA
}

