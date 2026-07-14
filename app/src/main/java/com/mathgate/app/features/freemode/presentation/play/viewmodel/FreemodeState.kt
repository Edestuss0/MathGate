package com.mathgate.app.features.freemode.presentation.play.viewmodel

import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion

data class FreemodeState(
    val question: FreemodeQuestion? = null,
    val answerInput: String = "",
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)

sealed class FreemodeEvent {
    data class OnAnswerInput(val input: String) : FreemodeEvent()
    data object OnAnswer : FreemodeEvent()
    data object OnBackClick : FreemodeEvent()
}

sealed class FreemodeEffect {
    data class SuccessSnackbar(val message: String) : FreemodeEffect()
    data class ErrorSnackbar(val message: String) : FreemodeEffect()
    data object NavigateBack : FreemodeEffect()
}