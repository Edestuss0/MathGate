package com.mathgate.app.features.user.data.freemode_data.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.mathgate.app.features.user.data.freemode_data.dao.FreemodeDataDao
import com.mathgate.app.features.user.data.freemode_data.entity.FreemodeDataEntity
import com.mathgate.app.features.user.domain.entity.FreemodeQuestionInput
import com.mathgate.app.shared.user.domain.entity.FreemodeStatsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FreemodeDataSource @Inject constructor(
    private val dao: FreemodeDataDao
) {
    fun getData(): Flow<List<FreemodeStatsItem>> {
        return dao.getData().map { it.map {
            FreemodeStatsItem(
                answer = it.answer,
                correct = it.isCorrect,
                difficulty = it.difficulty,
                date = it.date
            )
        } }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insert(data: FreemodeQuestionInput) {
        dao.insert(FreemodeDataEntity(
            isCorrect = data.isCorrect,
            answer = data.answer,
            difficulty = data.difficulty,
            date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        ))
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}