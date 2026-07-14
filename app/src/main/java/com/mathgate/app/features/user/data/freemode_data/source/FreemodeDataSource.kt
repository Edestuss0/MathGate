package com.mathgate.app.features.user.data.freemode_data.source

import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.user.data.freemode_data.dao.FreemodeDataDao
import com.mathgate.app.features.user.data.freemode_data.entity.FreemodeDataEntity
import com.mathgate.app.features.user.domain.entity.FreemodeQuestionInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FreemodeDataSource @Inject constructor(
    private val dao: FreemodeDataDao
) {
    fun getData(): Flow<List<Boolean>> {
        return dao.getData().map { it.map { it.isCorrect } }
    }

    suspend fun insert(data: FreemodeQuestionInput) {
        dao.insert(FreemodeDataEntity(
            isCorrect = data.isCorrect,
            answer = data.answer,
            difficulty = data.difficulty
        ))
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}