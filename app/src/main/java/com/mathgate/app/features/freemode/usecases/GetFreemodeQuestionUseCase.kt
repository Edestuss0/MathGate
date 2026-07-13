package com.mathgate.app.features.freemode.usecases

import com.mathgate.app.features.analytics.domain.repository.AnalyticsRepository
import com.mathgate.app.features.freemode.domain.entity.FreemodeAnalyticsEvent
import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.freemode.domain.repository.IFreemodeRepository
import javax.inject.Inject

class GetFreemodeQuestionUseCase @Inject constructor(
    private val repository: IFreemodeRepository,
    private val analytics: AnalyticsRepository
) {
    operator fun invoke(difficulty: FreemodeDifficulty): FreemodeQuestion {
        val question = repository.getQuestion(difficulty)
        analytics.log(FreemodeAnalyticsEvent.GetTheFreemodeQuestionEvent(question, difficulty))
        return question
    }
}