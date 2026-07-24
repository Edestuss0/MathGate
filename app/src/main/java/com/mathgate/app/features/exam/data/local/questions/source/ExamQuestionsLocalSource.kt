package com.mathgate.app.features.exam.data.local.questions.source

import androidx.room.Transaction
import com.mathgate.app.core.app.CACHE_LIVE_TIME
import com.mathgate.app.features.exam.data.local.questions.dao.ExamQuestionCacheDao
import com.mathgate.app.features.exam.data.local.questions.entity.ExamQuestionCacheEntity
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.shared.exam.entity.ExamTypes
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExamQuestionsLocalSource @Inject constructor(
    private val dao: ExamQuestionCacheDao
) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    suspend fun get(type: ExamTypes): ExamQuestion? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_LIVE_TIME)
        val cached = dao.get(type) ?: return null
        return decodeOrInvalidate(cached)
    }

    suspend fun getByNumber(type: ExamTypes, number: Int): ExamQuestion? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_LIVE_TIME)
        val cached = dao.getByNumber(type, number) ?: return null
        return decodeOrInvalidate(cached)
    }

    private suspend fun decodeOrInvalidate(cached: ExamQuestionCacheEntity): ExamQuestion? =
        runCatching { json.decodeFromString<ExamQuestion>(cached.json) }
            .getOrElse {
                dao.invalidate(cached.id)
                null
            }

    suspend fun insert(question: ExamQuestion, type: ExamTypes) {
        dao.insert(ExamQuestionCacheEntity(
            id = question.id,
            type = type,
            json = json.encodeToString(question),
            number = question.themeNumber
        ))
    }

    @Transaction
    suspend fun insertMore(questions: List<ExamQuestion>, type: ExamTypes) {
        questions.forEach { question ->
            dao.insert(ExamQuestionCacheEntity(
                id = question.id,
                type = type,
                json = json.encodeToString(question),
                number = question.themeNumber
            ))
        }
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun invalidate(id: Int) {
        dao.invalidate(id)
    }
}