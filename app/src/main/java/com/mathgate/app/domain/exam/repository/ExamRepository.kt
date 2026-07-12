package com.mathgate.app.domain.exam.repository

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTheme
import com.mathgate.app.domain.exam.entity.ExamTypes
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    suspend fun getExamQuestion(type: ExamTypes): Flow<AppResult<ExamQuestion>>
    suspend fun getExamThemes(type: ExamTypes): Flow<AppResult<List<ExamTheme>>>
    suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): Flow<AppResult<ExamQuestion>>
}