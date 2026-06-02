package com.mathgate.app.features.start_page

import com.mathgate.app.ui.components.AppSnackbarVisuals

data class StartPageState(
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null,
)