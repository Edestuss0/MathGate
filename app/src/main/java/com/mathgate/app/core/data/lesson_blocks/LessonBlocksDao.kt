package com.mathgate.app.core.data.lesson_blocks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LessonBlocksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonBlocks(blocks: List<LessonBlockEntity>)

    @Query("SELECT * FROM lesson_blocks WHERE pageId IN (SELECT id FROM lesson_pages WHERE lessonId = :lessonId)")
    suspend fun getBlocksByLessonId(lessonId: Int): List<LessonBlockEntity>
}