package com.mathgate.app.presentation.freemode.viewmodel

import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion

data class FreemodeState(
    val question: FreemodeQuestion? = null,
    val answerInput: String = "",
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val selectedDifficulty: FreemodeDifficulty = FreemodeDifficulty.MEDIUM
)