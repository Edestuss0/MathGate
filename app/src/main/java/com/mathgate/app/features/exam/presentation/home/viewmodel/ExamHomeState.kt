package com.mathgate.app.features.exam.presentation.home.viewmodel

import com.mathgate.app.shared.exam.entity.ExamTypes

data class ExamHomeState(
    val isError: Boolean = false,
    val selectedType: ExamTypes = ExamTypes.EGE
)

sealed class ExamHomeEvent {
    data class OnTypeSelect(val type: ExamTypes) : ExamHomeEvent()
    data object OnPlayClick : ExamHomeEvent()
    data object OnThemesClick : ExamHomeEvent()
}

sealed class ExamHomeEffects {
    data class OnPlayNavigate(val type: String) : ExamHomeEffects()
    data class OnThemesNavigate(val type: String) : ExamHomeEffects()
}