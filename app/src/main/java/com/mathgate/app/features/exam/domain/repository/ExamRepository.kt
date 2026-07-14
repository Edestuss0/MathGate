package com.mathgate.app.features.exam.domain.repository

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.features.exam.domain.entity.ExamTypes
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    suspend fun getExamQuestion(type: ExamTypes): Flow<AppResult<ExamQuestion>>
    suspend fun getExamThemes(type: ExamTypes): Flow<AppResult<List<ExamTheme>>>
    suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): Flow<AppResult<ExamQuestion>>
    fun preloadQuestions(type: ExamTypes, number: Int?, count: Int)
}