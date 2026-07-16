package com.mathgate.app.features.exam.domain.entity

import com.mathgate.app.core.analytics.entity.AnalyticsEvent
import com.mathgate.app.shared.exam.entity.ExamTypes

sealed class ExamAnalyticsEvent : AnalyticsEvent {
    data class GetTheQuestionEvent(val question: ExamQuestion, val type: ExamTypes) : ExamAnalyticsEvent() {
        override val name = "exam_question_getted"
        override val params = mapOf(
            "id" to question.id,
            "answer" to question.answer,
            "solution_blocks" to question.solutionBlocks.size,
            "blocks" to question.blocks.size,
            "type" to type.label,
            "theme_label" to question.themeLabel,
            "theme_number" to question.themeNumber
        )
    }

    data class GetTheExamThemeEvent(val theme: ExamTheme, val type: ExamTypes) : ExamAnalyticsEvent() {
        override val name = "exam_theme_getted"
        override val params = mapOf(
            "type" to type.label,
            "theme_label" to theme.label,
            "theme_number" to theme.number,
            "number_of_tasks" to theme.tasks
        )
    }

    data class OnAnswerTheExamQuestionEvent(val question: ExamQuestion, val type: ExamTypes, val isCorrect: Boolean) : ExamAnalyticsEvent() {
        override val name = "exam_question_answered"
        override val params = mapOf(
            "correct" to isCorrect,
            "id" to question.id,
            "answer" to question.answer,
            "solution_blocks" to question.solutionBlocks.size,
            "blocks" to question.blocks.size,
            "type" to type.label,
            "theme_label" to question.themeLabel,
            "theme_number" to question.themeNumber
        )
    }
}