package com.mathgate.app.features.user.presentation.start_page.viewmodel

import com.mathgate.app.features.user.presentation.start_page.viewmodel.StartPageEvent
import com.mathgate.app.ui.components.AppSnackbarVisuals

data class StartPageState(
    val isError: Boolean = false,
    val nameInput: String = ""
)

sealed class StartPageEvent {
    data class OnInputName(val input: String) : StartPageEvent()
    data object Register : StartPageEvent()
}

sealed class StartPageEffects {
    data class SuccessSnackbar(val message: String) : StartPageEffects()
    data class ErrorSnackbar(val message: String) : StartPageEffects()
}
