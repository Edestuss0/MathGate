package com.mathgate.app.domain.exam.data.local.themes.source

import com.mathgate.app.core.app.CACHE_LIVE_TIME
import com.mathgate.app.domain.exam.data.local.themes.dao.ExamThemeCacheDao
import com.mathgate.app.domain.exam.data.local.themes.entity.ExamThemeCacheEntity
import com.mathgate.app.domain.exam.entity.ExamTheme
import com.mathgate.app.domain.exam.entity.ExamTypes
import javax.inject.Inject
import kotlin.collections.forEach

class ExamThemeLocalSource @Inject constructor(
  private val dao: ExamThemeCacheDao
) {
    suspend fun get(type: ExamTypes): List<ExamTheme> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_LIVE_TIME)
        val cached = dao.getAll(type)
        return cached.map { ExamTheme(
            number = it.number,
            label = it.label,
            tasks = it.tasks
        ) }
    }

    suspend fun insert(theme: ExamTheme, type: ExamTypes) {
        dao.insert(ExamThemeCacheEntity(
            number = theme.number,
            label = theme.label,
            tasks = theme.tasks,
            type = type
        ))
    }

    suspend fun insertMore(themes: List<ExamTheme>, type: ExamTypes) {
        themes.forEach { theme ->
            dao.insert(ExamThemeCacheEntity(
                number = theme.number,
                label = theme.label,
                tasks = theme.tasks,
                type = type
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