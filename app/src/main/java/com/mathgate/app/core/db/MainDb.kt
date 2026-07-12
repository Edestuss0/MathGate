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
import com.mathgate.app.domain.exam.data.local.questions.dao.ExamQuestionCacheDao
import com.mathgate.app.domain.exam.data.local.questions.entity.ExamQuestionCacheEntity
import com.mathgate.app.domain.exam.data.local.themes.dao.ExamThemeCacheDao
import com.mathgate.app.domain.exam.data.local.themes.entity.ExamThemeCacheEntity
import com.mathgate.app.domain.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.domain.user.data.exam_data.entity.ExamDataEntity
import com.mathgate.app.domain.user.data.freemode_data.dao.FreemodeDataDao
import com.mathgate.app.domain.user.data.freemode_data.entity.FreemodeDataEntity

@Database(
    entities = [ThemeEntity::class, LessonEntity::class, LessonPageEntity::class, LessonBlockEntity::class, ExamQuestionCacheEntity::class, FreemodeDataEntity::class, ExamDataEntity::class, ExamThemeCacheEntity::class],
    version = 11,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract val themeDao: ThemeDao
    abstract val lessonDao: LessonDao
    abstract val lessonPageDao: LessonPageDao
    abstract val lessonBlockDao: LessonBlocksDao
    abstract val examQuestionCacheDao: ExamQuestionCacheDao
    abstract val examDataDao: ExamDataDao
    abstract val freemodeDataDao: FreemodeDataDao
    abstract val examThemeCacheDao: ExamThemeCacheDao

}