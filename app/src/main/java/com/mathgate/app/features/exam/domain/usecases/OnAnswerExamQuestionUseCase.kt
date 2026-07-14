package com.mathgate.app.features.exam.domain.usecases

import com.mathgate.app.core.analytics.core.AnalyticsManager
import com.mathgate.app.features.exam.domain.entity.ExamAnalyticsEvent
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTypes
import javax.inject.Inject

class OnAnswerExamQuestionUseCase @Inject constructor(
    private val analytics: AnalyticsManager
) {
    operator fun invoke(question: ExamQuestion, type: ExamTypes, isCorrect: Boolean) {
        analytics.log(ExamAnalyticsEvent.OnAnswerTheExamQuestionEvent(question, type, isCorrect))
    }
}