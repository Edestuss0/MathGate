package com.mathgate.app.shared.user.domain.usecases

import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTypes
import com.mathgate.app.features.user.domain.entity.ExamQuestionInput
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import javax.inject.Inject

class CompleteExamUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(isCorrect: Boolean, question: ExamQuestion, type: ExamTypes) {
        repository.completeExam(ExamQuestionInput(
            isCorrect = isCorrect,
            answer = question.answer,
            themeLabel = question.themeLabel,
            themeNubmer = question.themeNumber,
            type = type.label
        ))
    }
}