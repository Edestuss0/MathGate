package com.mathgate.app.features.freemode.viewmodel

import com.mathgate.app.domain.freemode.entity.FreemodeDifficulty
import com.mathgate.app.domain.freemode.entity.FreemodeQuestion
import com.mathgate.app.domain.user.entity.User

data class FreemodeState(
    val question: FreemodeQuestion? = null,
    val answerInput: String = "",
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val selectedDifficulty: FreemodeDifficulty = FreemodeDifficulty.MEDIUM
)