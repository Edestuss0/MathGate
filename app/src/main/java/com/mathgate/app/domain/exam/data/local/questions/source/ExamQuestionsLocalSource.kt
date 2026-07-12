package com.mathgate.app.domain.exam.data.local.questions.source

import com.mathgate.app.core.app.CACHE_LIVE_TIME
import com.mathgate.app.domain.exam.data.local.questions.dao.ExamQuestionCacheDao
import com.mathgate.app.domain.exam.data.local.questions.entity.ExamQuestionCacheEntity
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTypes
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExamQuestionsLocalSource @Inject constructor(
    private val dao: ExamQuestionCacheDao
) {

    suspend fun get(type: ExamTypes): ExamQuestion? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_LIVE_TIME)
        val cached = dao.get(type)
        if (cached == null) return null
        return Json.decodeFromString<ExamQuestion>(cached.json)
    }

    suspend fun getByNumber(type: ExamTypes, number: Int): ExamQuestion? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_LIVE_TIME)
        val cached = dao.getByNumber(type, number)
        if (cached == null) return null
        return Json.decodeFromString<ExamQuestion>(cached.json)
    }

    suspend fun insert(question: ExamQuestion, type: ExamTypes) {
        dao.insert(ExamQuestionCacheEntity(
            id = question.id,
            type = type,
            json = Json.encodeToString(question),
            number = question.themeNumber
        ))
    }

    suspend fun insertMore(questions: List<ExamQuestion>, type: ExamTypes) {
        questions.forEach { question ->
            dao.insert(ExamQuestionCacheEntity(
                id = question.id,
                type = type,
                json = Json.encodeToString(question),
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