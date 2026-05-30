package com.mathgate.app.core.data.lesson_tasks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonTaskDao {

    @Query("SELECT * FROM lesson_tasks")
    fun getAllLessonTasks(): Flow<List<LessonTaskEntity>>

    @Query("SELECT * FROM lesson_tasks WHERE lessonId = :id")
    suspend fun getTasksByLessonId(id: Int): List<LessonTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonTasks(tasks: List<LessonTaskEntity>)
}