package com.mathgate.app.features.exam.viewmodel

import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTheme
import com.mathgate.app.domain.exam.entity.ExamTypes

data class ExamState(
    val isError: Boolean = false,
    val question: ExamQuestion? = null,
    val isLoading: Boolean = false,
    val isAnswered: Boolean = false,
    val type: ExamTypes? = null,
    val themes: List<ExamTheme>? = null
)