package com.mathgate.app.features.user.domain.entity

data class FreemodeQuestionInput(
    val answer: String,
    val isCorrect: Boolean,
    val difficulty: String
)
