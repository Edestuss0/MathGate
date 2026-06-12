package com.mathgate.app.core.data.lessons

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mathgate.app.core.data.education.LessonWithPages
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE themeId = :id")
    suspend fun getLessonsByThemeId(id: Int): List<LessonEntity>

    @Transaction
    @Query("SELECT * FROM lessons WHERE id = :id LIMIT 1")
    suspend fun getLessonWithPages(id: Int): LessonWithPages?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)
}