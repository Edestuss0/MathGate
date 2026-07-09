package com.mathgate.app.features.freemode.viewmodel

import com.mathgate.app.domain.freemode.entity.FreemodeQuestion

data class FreemodeState(
    val question: FreemodeQuestion? = null,
    val answerInput: String = "",
    val streak: Int = 0,
    val isError: Boolean = false,
)