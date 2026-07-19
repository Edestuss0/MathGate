package com.mathgate.app.shared.user.domain.entity

import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty

data class FreemodeStatsItem(
    val correct: Boolean,
    val date: String,
    val answer: String,
    val difficulty: FreemodeDifficulty
)