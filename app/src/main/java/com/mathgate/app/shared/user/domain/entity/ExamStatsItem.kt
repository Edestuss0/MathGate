package com.mathgate.app.shared.user.domain.entity

import com.mathgate.app.shared.exam.entity.ExamTypes

data class ExamStatsItem(
    val correct: Boolean,
    val date: String,
    val answer: String,
    val themeLabel: String,
    val themeNumber: Int,
    val type: ExamTypes
)