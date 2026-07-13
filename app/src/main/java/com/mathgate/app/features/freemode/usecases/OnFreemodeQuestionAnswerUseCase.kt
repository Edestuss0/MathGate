package com.mathgate.app.features.freemode.usecases

import com.mathgate.app.features.analytics.domain.repository.AnalyticsRepository
import com.mathgate.app.features.freemode.domain.entity.FreemodeAnalyticsEvent
import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import javax.inject.Inject

class OnFreemodeQuestionAnswerUseCase @Inject constructor(
    private val analytics: AnalyticsRepository
) {
    operator fun invoke(question: FreemodeQuestion, difficulty: FreemodeDifficulty, isCorrect: Boolean) {
        analytics.log(FreemodeAnalyticsEvent.OnAnswerFreemodeQuestionEvent(question, difficulty, isCorrect))
    }
}