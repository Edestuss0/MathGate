package com.mathgate.app.domain.user.data.freemode_data.source

import com.mathgate.app.domain.freemode.entity.FreemodeQuestion
import com.mathgate.app.domain.user.data.freemode_data.dao.FreemodeDataDao
import com.mathgate.app.domain.user.data.freemode_data.entity.FreemodeDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FreemodeDataSource @Inject constructor(
    private val dao: FreemodeDataDao
) {
    fun getData(): Flow<List<Boolean>> {
        return dao.getData().map { it.map { it.isCorrect } }
    }

    suspend fun insert(isCorrect: Boolean, data: FreemodeQuestion) {
        dao.insert(FreemodeDataEntity(
            isCorrect = isCorrect,
            answer = data.answer
        ))
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}