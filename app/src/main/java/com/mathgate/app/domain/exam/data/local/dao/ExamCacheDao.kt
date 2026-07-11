package com.mathgate.app.domain.exam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mathgate.app.domain.exam.data.local.entities.ExamCacheEntity
import com.mathgate.app.domain.exam.entity.ExamTypes

@Dao
interface ExamCacheDao {

    @Query("SELECT * FROM exam_cache WHERE type = :type LIMIT 1")
    suspend fun get(type: ExamTypes): ExamCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExamCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMore(entity: List<ExamCacheEntity>)

    @Query("DELETE FROM exam_cache WHERE cachedAt < :time")
    suspend fun clearExpired(time: Long)

    @Query("DELETE FROM exam_cache")
    suspend fun clearAll()

    @Query("DELETE FROM exam_cache WHERE id = :id")
    suspend fun invalidate(id: Int)
}