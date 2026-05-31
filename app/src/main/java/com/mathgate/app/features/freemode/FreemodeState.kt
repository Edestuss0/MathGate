package com.mathgate.app.features.freemode

import com.mathgate.app.ui.components.AppSnackbarVisuals

data class FreemodeState(
    val question: String = "",
    val answerInput: String = "",
    val streak: Int = 0,
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null
)