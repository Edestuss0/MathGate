package com.mathgate.app.features.user.data.exam_data.source

import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.features.user.data.exam_data.entity.ExamDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExamDataSource @Inject constructor(
    private val dao: ExamDataDao
) {
    fun getData(): Flow<List<Boolean>> {
        return dao.getData().map { it.map { it.isCorrect } }
    }

    suspend fun insert(isCorrect: Boolean, data: ExamQuestion) {
        dao.insert(
            ExamDataEntity(
                isCorrect = isCorrect,
                answer = data.answer
            )
        )
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}