package com.mathgate.app.core.db.di

import android.content.Context
import androidx.room.Room
import com.mathgate.app.core.db.core.CacheDb
import com.mathgate.app.core.db.core.UserDb
import com.mathgate.app.features.exam.data.local.questions.dao.ExamQuestionCacheDao
import com.mathgate.app.features.exam.data.local.themes.dao.ExamThemeCacheDao
import com.mathgate.app.features.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.features.user.data.freemode_data.dao.FreemodeDataDao
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
    fun provideUserDb(
        @ApplicationContext context: Context
    ): UserDb = Room.databaseBuilder(
            context.applicationContext,
            UserDb::class.java,
            "user.db",
        ).build()

    @Provides @Singleton
    fun provideCacheDb(
        @ApplicationContext context: Context
    ): CacheDb = Room.databaseBuilder(
        context.applicationContext,
        CacheDb::class.java,
        "cache.db",
    ).fallbackToDestructiveMigration().build()


    @Provides @Singleton
    fun provideExamDataDao(
        db: UserDb
    ): ExamDataDao = db.examDataDao

    @Provides @Singleton
    fun provideFreemodeDataDao(
        db: UserDb
    ): FreemodeDataDao = db.freemodeDataDao

    @Provides @Singleton
    fun provideExamCacheDao(
        db: CacheDb
    ): ExamQuestionCacheDao = db.examQuestionCacheDao

    @Provides @Singleton
    fun provideExamThemeCacheDao(
        db: CacheDb
    ): ExamThemeCacheDao = db.examThemeCacheDao
}
