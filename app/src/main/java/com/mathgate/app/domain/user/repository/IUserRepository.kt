package com.mathgate.app.domain.user.repository

import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.freemode.entity.FreemodeQuestion
import com.mathgate.app.domain.user.entity.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUser(): Flow<User>
    suspend fun getAuthorizedStatus(): Flow<Boolean>
    suspend fun completeFreemode(isCorrect: Boolean, question: FreemodeQuestion)
    suspend fun completeExam(isCorrect: Boolean, question: ExamQuestion)
    suspend fun deleteAccount()
    suspend fun register(name: String)
}