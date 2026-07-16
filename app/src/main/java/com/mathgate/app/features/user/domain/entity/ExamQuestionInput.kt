package com.mathgate.app.features.user.domain.entity

import com.mathgate.app.shared.exam.entity.ExamTypes

data class ExamQuestionInput(
    val answer: String,
    val isCorrect: Boolean,
    val themeLabel: String,
    val themeNubmer: Int,
    val type: ExamTypes
)
