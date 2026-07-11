package com.mathgate.app.domain.exam.data.local.source

import com.mathgate.app.core.app.CACHE_LIVE_TIME
import com.mathgate.app.domain.exam.data.local.dao.ExamCacheDao
import com.mathgate.app.domain.exam.data.local.entities.ExamCacheEntity
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTypes
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExamLocalSource @Inject constructor(
    private val dao: ExamCacheDao
) {

    suspend fun get(type: ExamTypes): ExamQuestion? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_LIVE_TIME)
        val cached = dao.get(type)
        if (cached == null) return null
        return Json.decodeFromString<ExamQuestion>(cached.json)
    }

    suspend fun insert(question: ExamQuestion, type: ExamTypes) {
        dao.insert(ExamCacheEntity(
            id = question.id,
            type = type,
            json = Json.encodeToString(question)
        ))
    }

    suspend fun insertMore(questions: List<ExamQuestion>, type: ExamTypes) {
        questions.forEach { question ->
            dao.insert(ExamCacheEntity(
                    id = question.id,
                    type = type,
                    json = Json.encodeToString(question)
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