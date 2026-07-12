package com.mathgate.app.domain.exam.usecases

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTypes
import com.mathgate.app.domain.exam.repository.ExamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExamQuestionByNumberUseCase @Inject constructor(
    private val repository: ExamRepository
) {
    suspend operator fun invoke(types: ExamTypes, number: Int): Flow<AppResult<ExamQuestion>> = repository.getExamQuestionByNumber(types, number)
}