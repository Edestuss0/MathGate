package com.mathgate.app.features.user.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Transaction
import com.mathgate.app.core.di.ApplicationScope
import com.mathgate.app.features.user.data.exam_data.source.ExamDataSource
import com.mathgate.app.features.user.data.freemode_data.source.FreemodeDataSource
import com.mathgate.app.features.user.data.user_data.source.UserSource
import com.mathgate.app.features.user.domain.entity.ExamQuestionInput
import com.mathgate.app.features.user.domain.entity.FreemodeQuestionInput
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userData: UserSource,
    private val examData: ExamDataSource,
    private val freemodeData: FreemodeDataSource,
    @ApplicationScope private val scope: CoroutineScope
) : IUserRepository {

    private val user = combine(
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

    override fun getUser(): StateFlow<User?> = user.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    @Transaction
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun completeFreemode(question: FreemodeQuestionInput) {
        if (question.isCorrect) {
            userData.addExpFreemode(question.difficulty)
            userData.updateStreak()
        } else {
            userData.failStreak()
        }
        freemodeData.insert(question)
    }

    @Transaction
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun completeExam(question: ExamQuestionInput) {
        if (question.isCorrect) {
            userData.addExpExam(question.type)
            userData.updateStreak()
        } else {
            userData.failStreak()
        }
        examData.insert(question)
    }

    @Transaction
    override suspend fun deleteAccount() {
        userData.deleteAccount()
        examData.clearAll()
        freemodeData.clearAll()
    }

    override suspend fun register(name: String) {
        userData.register(name)
    }
}