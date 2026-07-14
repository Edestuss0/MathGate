package com.mathgate.app.features.exam.presentation.play.viewmodel

import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.features.exam.domain.entity.ExamTypes

data class ExamPlayState(
    val isError: Boolean = false,
    val question: ExamQuestion? = null,
    val isLoading: Boolean = false,
    val isSkipped: Boolean = false,
    val answerInput: String = ""
)

sealed class ExamPlayEvent {
    data class OnAnswerInput(val input: String) : ExamPlayEvent()
    data object OnAnswer : ExamPlayEvent()
    data object OnSkip : ExamPlayEvent()
    data object OnBackClick : ExamPlayEvent()
    data object OnGetNewQuestion : ExamPlayEvent()
}

sealed class ExamPlayEffect {
    data class SuccessSnackbar(val message: String) : ExamPlayEffect()
    data class ErrorSnackbar(val message: String) : ExamPlayEffect()
    data object NavigateBack : ExamPlayEffect()
}