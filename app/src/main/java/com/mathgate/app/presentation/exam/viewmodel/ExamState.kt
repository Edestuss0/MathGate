package com.mathgate.app.presentation.exam.viewmodel

import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.features.exam.domain.entity.ExamTypes

data class ExamState(
    val isError: Boolean = false,
    val question: ExamQuestion? = null,
    val isLoading: Boolean = false,
    val isAnswered: Boolean = false,
    val type: ExamTypes? = null,
    val themes: List<ExamTheme>? = null
)