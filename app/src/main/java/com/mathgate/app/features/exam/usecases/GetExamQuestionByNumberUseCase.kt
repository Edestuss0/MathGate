package com.mathgate.app.features.exam.usecases

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.features.analytics.domain.repository.AnalyticsRepository
import com.mathgate.app.features.exam.domain.entity.ExamAnalyticsEvent
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTypes
import com.mathgate.app.features.exam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExamQuestionByNumberUseCase @Inject constructor(
    private val repository: ExamRepository,
    private val analytics: AnalyticsRepository
) {
    operator fun invoke(type: ExamTypes, number: Int): Flow<AppResult<ExamQuestion>> = flow {
        repository.getExamQuestionByNumber(type, number).collect { result ->
            when {
                result is AppResult.Success -> {
                    val event = ExamAnalyticsEvent.GetTheQuestionEvent(result.data, type)
                    analytics.log(event)
                    emit(result)
                }
                else -> {
                    emit(result)
                }
            }
        }
    }
}