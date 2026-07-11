package com.mathgate.app.core.db

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
import com.mathgate.app.domain.exam.data.local.dao.ExamCacheDao
import com.mathgate.app.domain.exam.data.local.entities.ExamCacheEntity
import com.mathgate.app.domain.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.domain.user.data.exam_data.entity.ExamDataEntity
import com.mathgate.app.domain.user.data.freemode_data.dao.FreemodeDataDao
import com.mathgate.app.domain.user.data.freemode_data.entity.FreemodeDataEntity

@Database(
    entities = [ThemeEntity::class, LessonEntity::class, LessonPageEntity::class, LessonBlockEntity::class, ExamCacheEntity::class, FreemodeDataEntity::class, ExamDataEntity::class],
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
    abstract val examDataDao: ExamDataDao
    abstract val freemodeDataDao: FreemodeDataDao

}