package com.mathgate.app.features.user.domain.entity

data class ExamQuestionInput(
    val answer: String,
    val isCorrect: Boolean,
    val themeLabel: String,
    val themeNubmer: Int,
    val type: String
)
