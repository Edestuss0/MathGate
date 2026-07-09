package com.mathgate.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mathgate.app.core.data.lesson_blocks.LessonBlockEntity
import com.mathgate.app.core.data.lesson_blocks.LessonBlocksDao
import com.mathgate.app.core.data.lesson_pages.LessonPageDao
import com.mathgate.app.core.data.lesson_pages.LessonPageEntity
import com.mathgate.app.core.data.theme.ThemeDao
import com.mathgate.app.core.data.theme.ThemeEntity
import com.mathgate.app.core.data.lessons.LessonDao
import com.mathgate.app.core.data.lessons.LessonEntity
import com.mathgate.app.data.features.exam.local.dao.ExamCacheDao
import com.mathgate.app.data.features.exam.local.entities.ExamCacheEntity
import com.mathgate.app.domain.exam.entity.ExamTypes

@Database(
    entities = [ThemeEntity::class, LessonEntity::class, LessonPageEntity::class, LessonBlockEntity::class, ExamCacheEntity::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract val themeDao: ThemeDao
    abstract val lessonDao: LessonDao
    abstract val lessonPageDao: LessonPageDao
    abstract val lessonBlockDao: LessonBlocksDao
    abstract val examCacheDao: ExamCacheDao

}