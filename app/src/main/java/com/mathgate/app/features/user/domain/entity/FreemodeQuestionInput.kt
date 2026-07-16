package com.mathgate.app.features.user.domain.entity

import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty

data class FreemodeQuestionInput(
    val answer: String,
    val isCorrect: Boolean,
    val difficulty: FreemodeDifficulty
)
