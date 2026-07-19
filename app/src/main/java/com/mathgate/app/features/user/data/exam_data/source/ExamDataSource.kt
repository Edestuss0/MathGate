package com.mathgate.app.features.user.data.exam_data.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.mathgate.app.features.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.features.user.data.exam_data.entity.ExamDataEntity
import com.mathgate.app.features.user.domain.entity.ExamQuestionInput
import com.mathgate.app.shared.user.domain.entity.ExamStatsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExamDataSource @Inject constructor(
    private val dao: ExamDataDao
) {
    fun getData(): Flow<List<ExamStatsItem>> {
        return dao.getData().map {
            it.map { entity ->
                ExamStatsItem(
                    answer = entity.answer,
                    themeLabel = entity.themeLabel,
                    themeNumber = entity.themeNumber,
                    type = entity.type,
                    correct = entity.isCorrect,
                    date = entity.date
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insert(data: ExamQuestionInput) {
        dao.insert(
            ExamDataEntity(
                isCorrect = data.isCorrect,
                answer = data.answer,
                themeLabel = data.themeLabel,
                themeNumber = data.themeNubmer,
                type = data.type,
                date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            )
        )
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}