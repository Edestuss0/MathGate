package com.mathgate.app.shared.user.domain.repository

import com.mathgate.app.features.user.domain.entity.ExamQuestionInput
import com.mathgate.app.features.user.domain.entity.FreemodeQuestionInput
import com.mathgate.app.shared.user.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IUserRepository {
    fun getUser(): StateFlow<User?>
    suspend fun completeFreemode(question: FreemodeQuestionInput)
    suspend fun completeExam(question: ExamQuestionInput)
    suspend fun deleteAccount()
    suspend fun register(name: String)
}