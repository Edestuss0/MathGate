package com.mathgate.app.features.exam.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class ExamQuestion (
    val id: Int,
    val answer: String,
    val blocks: List<ExamBlock>,
    val solutionBlocks: List<ExamBlock>,
    val themeNumber: Int,
    val themeLabel: String
)