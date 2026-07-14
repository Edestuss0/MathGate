package com.mathgate.app.features.exam.presentation.themes.viewmodel

import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.features.exam.domain.entity.ExamTypes

data class ExamThemesState(
    val isLoading: Boolean = false,
    val themes: List<ExamTheme>? = null
)

sealed class ExamThemesEvent {
    data class OnPlayClick(val number: Int) : ExamThemesEvent()
    data object OnBackClick : ExamThemesEvent()
}

sealed class ExamThemesEffect {
    data class PlayNavigate(val type: String,  val number: Int) : ExamThemesEffect()
    data object BackNavigate : ExamThemesEffect()
    data class ErrorSnackbar(val message: String) : ExamThemesEffect()
}