package com.mathgate.app.features.exam.usecases

import com.mathgate.app.features.analytics.domain.repository.AnalyticsRepository
import com.mathgate.app.features.exam.domain.entity.ExamAnalyticsEvent
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTypes
import javax.inject.Inject

class OnAnswerExamQuestionUseCase @Inject constructor(
    private val analytics: AnalyticsRepository
) {
    operator fun invoke(question: ExamQuestion, type: ExamTypes, isCorrect: Boolean) {
        analytics.log(ExamAnalyticsEvent.OnAnswerTheExamQuestionEvent(question, type, isCorrect))
    }
}