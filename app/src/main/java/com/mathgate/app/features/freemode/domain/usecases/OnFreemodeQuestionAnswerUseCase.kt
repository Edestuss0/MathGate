package com.mathgate.app.features.freemode.domain.usecases

import com.mathgate.app.core.analytics.core.AnalyticsManager
import com.mathgate.app.features.freemode.domain.entity.FreemodeAnalyticsEvent
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import javax.inject.Inject

class OnFreemodeQuestionAnswerUseCase @Inject constructor(
    private val analytics: AnalyticsManager
) {
    operator fun invoke(question: FreemodeQuestion, difficulty: FreemodeDifficulty, isCorrect: Boolean) {
        analytics.log(FreemodeAnalyticsEvent.OnAnswerFreemodeQuestionEvent(question, difficulty, isCorrect))
    }
}