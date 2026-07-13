package com.mathgate.app.features.exam.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class ExamBlock (
    val type: ExamBlockType,
    val content: String,
)

enum class ExamBlockType {
    TEXT, IMAGE, FORMULA
}

