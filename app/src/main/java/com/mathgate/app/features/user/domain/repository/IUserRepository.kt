package com.mathgate.app.features.user.domain.repository

import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.user.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUser(): Flow<User>
    suspend fun getAuthorizedStatus(): Flow<Boolean>
    suspend fun completeFreemode(isCorrect: Boolean, question: FreemodeQuestion)
    suspend fun completeExam(isCorrect: Boolean, question: ExamQuestion)
    suspend fun deleteAccount()
    suspend fun register(name: String)
}