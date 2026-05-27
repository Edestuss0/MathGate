package com.mathgate.app.features.freemode

data class FreemodeState(
    val question: String = "",
    val answerInput: String = "",
    val streak: Int = 0,
    val isError: Boolean = false,
    val message: String? = null
)