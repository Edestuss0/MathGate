package com.mathgate.app.domain.exam.usecases

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.domain.exam.entity.ExamTheme
import com.mathgate.app.domain.exam.entity.ExamTypes
import com.mathgate.app.domain.exam.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExamThemesUseCase @Inject constructor(
    private val repository: ExamRepository
) {
    suspend operator fun invoke(type: ExamTypes): Flow<AppResult<List<ExamTheme>>> = repository.getExamThemes(type)
}