package com.mathgate.app.features.user.data.exam_data.source

import com.mathgate.app.features.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.features.user.data.exam_data.entity.ExamDataEntity
import com.mathgate.app.features.user.domain.entity.ExamQuestionInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExamDataSource @Inject constructor(
    private val dao: ExamDataDao
) {
    fun getData(): Flow<List<Boolean>> {
        return dao.getData().map { it.map { it.isCorrect } }
    }

    suspend fun insert(data: ExamQuestionInput) {
        dao.insert(
            ExamDataEntity(
                isCorrect = data.isCorrect,
                answer = data.answer,
                themeLabel = data.themeLabel,
                themeNubmer = data.themeNubmer,
                type = data.type
            )
        )
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}