package com.mathgate.app.features.exam.data.local.questions.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mathgate.app.features.exam.data.local.questions.entity.ExamQuestionCacheEntity
import com.mathgate.app.features.exam.domain.entity.ExamTypes

@Dao
interface ExamQuestionCacheDao {

    @Query("SELECT * FROM exam_cache WHERE type = :type LIMIT 1")
    suspend fun get(type: ExamTypes): ExamQuestionCacheEntity?

    @Query("SELECT * FROM exam_cache WHERE type = :type AND number = :number LIMIT 1")
    suspend fun getByNumber(type: ExamTypes, number: Int): ExamQuestionCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExamQuestionCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMore(entity: List<ExamQuestionCacheEntity>)

    @Query("DELETE FROM exam_cache WHERE cachedAt < :time")
    suspend fun clearExpired(time: Long)

    @Query("DELETE FROM exam_cache")
    suspend fun clearAll()

    @Query("DELETE FROM exam_cache WHERE id = :id")
    suspend fun invalidate(id: Int)
}