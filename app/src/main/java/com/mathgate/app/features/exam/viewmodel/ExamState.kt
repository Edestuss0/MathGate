package com.mathgate.app.features.exam.viewmodel

import com.mathgate.app.core.entities.OgeQuestion
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.ui.components.AppSnackbarVisuals

data class ExamState(
    val isError: Boolean = false,
    val question: ExamQuestion? = null,
    val isLoading: Boolean = false,
    val isAnswered: Boolean = false
)