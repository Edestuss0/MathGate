package com.mathgate.app.core.di

import android.content.Context
import androidx.room.Room
import com.mathgate.app.core.data.campaign.CampaignDao
import com.mathgate.app.core.data.db.MainDb
import com.mathgate.app.core.data.education.EducationDao
import com.mathgate.app.core.data.lesson_tasks.LessonTaskDao
import com.mathgate.app.core.data.lessons.LessonDao
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
    fun provideCampaignDao(
        db: MainDb
    ): CampaignDao = db.campaignDao

    @Provides @Singleton
    fun provideEducationDao(
        db: MainDb
    ): EducationDao = db.educationDao

    @Provides @Singleton
    fun provideLessonDao(
        db: MainDb
    ): LessonDao = db.lessonDao

    @Provides @Singleton
    fun provideLessonTasksDao(
        db: MainDb
    ): LessonTaskDao = db.lessonTaskDao
}


