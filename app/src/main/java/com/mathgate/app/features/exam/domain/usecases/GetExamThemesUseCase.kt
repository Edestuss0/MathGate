package com.mathgate.app.features.exam.domain.usecases

import com.mathgate.app.core.analytics.core.AnalyticsManager
import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.features.exam.domain.entity.ExamAnalyticsEvent
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.features.exam.domain.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExamThemesUseCase @Inject constructor(
    private val repository: ExamRepository,
    private val analytics: AnalyticsManager
) {
    operator fun invoke(type: ExamTypes): Flow<AppResult<List<ExamTheme>>> = flow {
        repository.getExamThemes(type).collect { result ->
            when {
                result is AppResult.Success -> {
                    result.data.forEach {
                        val event = ExamAnalyticsEvent.GetTheExamThemeEvent(it, type)
                        analytics.log(event)
                    }
                    emit(result)
                }
                else -> {
                    emit(result)
                }
            }
        }
    }
}