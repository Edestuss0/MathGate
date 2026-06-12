package com.mathgate.app.core.data.lesson_pages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface LessonPageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonPages(pages: List<LessonPageEntity>)
}