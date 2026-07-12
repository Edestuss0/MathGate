package com.mathgate.app.core.di

import android.content.Context
import androidx.room.Room
import com.mathgate.app.core.db.MainDb
import com.mathgate.app.core.data.lesson_blocks.LessonBlocksDao
import com.mathgate.app.core.data.lesson_pages.LessonPageDao
import com.mathgate.app.core.data.theme.ThemeDao
import com.mathgate.app.core.data.lessons.LessonDao
import com.mathgate.app.domain.exam.data.local.questions.dao.ExamQuestionCacheDao
import com.mathgate.app.domain.exam.data.local.themes.dao.ExamThemeCacheDao
import com.mathgate.app.domain.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.domain.user.data.freemode_data.dao.FreemodeDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): MainDb = Room.databaseBuilder(
            context.applicationContext,
            MainDb::class.java,
            "main.db",
        ).fallbackToDestructiveMigration().build()

    @Provides @Singleton
    fun provideEducationDao(
        db: MainDb
    ): ThemeDao = db.themeDao

    @Provides @Singleton
    fun provideLessonDao(
        db: MainDb
    ): LessonDao = db.lessonDao

    @Provides @Singleton
    fun provideLessonPagesDao(
        db: MainDb
    ): LessonPageDao = db.lessonPageDao

    @Provides @Singleton
    fun provideLessonBlocksDao(
        db: MainDb
    ): LessonBlocksDao = db.lessonBlockDao

    @Provides @Singleton
    fun provideExamCacheDao(
        db: MainDb
    ): ExamQuestionCacheDao = db.examQuestionCacheDao

    @Provides @Singleton
    fun provideExamThemeCacheDao(
        db: MainDb
    ): ExamThemeCacheDao = db.examThemeCacheDao

    @Provides @Singleton
    fun provideExamDataDao(
        db: MainDb
    ): ExamDataDao = db.examDataDao

    @Provides @Singleton
    fun provideFreemodeDataDao(
        db: MainDb
    ): FreemodeDataDao = db.freemodeDataDao
}


