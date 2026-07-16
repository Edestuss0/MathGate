package com.mathgate.app.features.exam.data.local.themes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mathgate.app.features.exam.data.local.themes.entity.ExamThemeCacheEntity
import com.mathgate.app.shared.exam.entity.ExamTypes

@Dao
interface ExamThemeCacheDao {
    @Query("SELECT * FROM exam_theme_cache WHERE type = :type")
    suspend fun getAll(type: ExamTypes): List<ExamThemeCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExamThemeCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMore(entity: List<ExamThemeCacheEntity>)

    @Query("DELETE FROM exam_theme_cache WHERE cachedAt < :time")
    suspend fun clearExpired(time: Long)

    @Query("DELETE FROM exam_theme_cache")
    suspend fun clearAll()

    @Query("DELETE FROM exam_theme_cache WHERE id = :id")
    suspend fun invalidate(id: Int)
}