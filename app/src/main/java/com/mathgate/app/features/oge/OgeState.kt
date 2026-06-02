package com.mathgate.app.features.oge

import com.mathgate.app.core.entities.OgeQuestion
import com.mathgate.app.ui.components.AppSnackbarVisuals

data class OgeState(
    val isError: Boolean = false,
    val question: OgeQuestion? = null,
    val snackbarMessage: AppSnackbarVisuals? = null,
    val isLoading: Boolean = false
)