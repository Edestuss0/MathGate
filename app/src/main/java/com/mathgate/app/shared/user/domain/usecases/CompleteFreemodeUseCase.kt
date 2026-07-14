package com.mathgate.app.shared.user.domain.usecases

import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.user.domain.entity.FreemodeQuestionInput
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import javax.inject.Inject

class CompleteFreemodeUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(isCorrect: Boolean, question: FreemodeQuestion, difficulty: FreemodeDifficulty) {
        repository.completeFreemode(
            question = FreemodeQuestionInput(
                answer = question.answer,
                isCorrect = isCorrect,
                difficulty = difficulty.label
            )
        )
    }
}