package com.mathgate.app.features.user.data.repository

import com.mathgate.app.features.user.data.exam_data.source.ExamDataSource
import com.mathgate.app.features.user.data.freemode_data.source.FreemodeDataSource
import com.mathgate.app.features.user.data.user_data.source.UserSource
import com.mathgate.app.features.user.domain.entity.ExamQuestionInput
import com.mathgate.app.features.user.domain.entity.FreemodeQuestionInput
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userData: UserSource,
    private val examData: ExamDataSource,
    private val freemodeData: FreemodeDataSource
) : IUserRepository {
    override fun getUser(): Flow<User> {

        return combine(
            userData.getProfile(),
            examData.getData(),
            freemodeData.getData()
        ) { user, exam, freemode ->
            User(
                username = user.username,
                level = user.level,
                experience = user.experience,
                bestStreak = user.bestStreak,
                currentStreak = user.currentStreak,
                registered = user.registered,
                examData = exam,
                freemodeData = freemode,
                levelUpLimit = user.levelUpLimit
            )
        }
    }

    override suspend fun completeFreemode(question: FreemodeQuestionInput) {
        if (question.isCorrect) {
            userData.addExpFreemode(question.difficulty)
            userData.updateStreak()
        } else {
            userData.failStreak()
        }
        freemodeData.insert(question)
    }

    override suspend fun completeExam(question: ExamQuestionInput) {
        if (question.isCorrect) {
            userData.addExpExam(question.type)
            userData.updateStreak()
        } else {
            userData.failStreak()
        }
        examData.insert(question)
    }

    override suspend fun deleteAccount() {
        userData.deleteAccount()
        examData.clearAll()
        freemodeData.clearAll()
    }

    override suspend fun register(name: String) {
        userData.register(name)
    }
}