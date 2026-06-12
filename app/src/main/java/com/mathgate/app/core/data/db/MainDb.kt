package com.mathgate.app.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mathgate.app.core.data.lesson_blocks.LessonBlockEntity
import com.mathgate.app.core.data.lesson_blocks.LessonBlocksDao
import com.mathgate.app.core.data.lesson_pages.LessonPageDao
import com.mathgate.app.core.data.lesson_pages.LessonPageEntity
import com.mathgate.app.core.data.theme.ThemeDao
import com.mathgate.app.core.data.theme.ThemeEntity
import com.mathgate.app.core.data.lessons.LessonDao
import com.mathgate.app.core.data.lessons.LessonEntity

@Database(
    entities = [ThemeEntity::class, LessonEntity::class, LessonPageEntity::class, LessonBlockEntity::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract val themeDao: ThemeDao
    abstract val lessonDao: LessonDao
    abstract val lessonPageDao: LessonPageDao
    abstract val lessonBlockDao: LessonBlocksDao

}