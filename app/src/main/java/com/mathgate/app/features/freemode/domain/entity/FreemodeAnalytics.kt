package com.mathgate.app.features.freemode.domain.entity

import com.mathgate.app.features.analytics.domain.entity.AnalyticsEvent

sealed class FreemodeAnalyticsEvent : AnalyticsEvent {
    data class GetTheFreemodeQuestionEvent(val question: FreemodeQuestion, val difficulty: FreemodeDifficulty) : FreemodeAnalyticsEvent() {
        override val name = "get_the_freemode_question_event"
        override val params = mapOf(
            "answer" to question.answer,
            "count_of_blocks" to question.blocks.size,
            "blocks_full" to question.blocks.toString(),
            "difficulty" to difficulty.label
        )
    }

    data class OnAnswerFreemodeQuestionEvent(val question: FreemodeQuestion, val difficulty: FreemodeDifficulty, val isCorrect: Boolean) : FreemodeAnalyticsEvent() {
        override val name = "freemode_question_answered"
        override val params = mapOf(
            "answer" to question.answer,
            "count_of_blocks" to question.blocks.size,
            "blocks_full" to question.blocks.toString(),
            "difficulty" to difficulty.label,
            "correct" to isCorrect
        )
    }
}