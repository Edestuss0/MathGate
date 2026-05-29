package com.mathgate.app.core.di

import android.content.Context
import androidx.room.Room
import com.mathgate.app.core.data.campaign.CampaignDao
import com.mathgate.app.core.data.db.MainDb
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
    ): CampaignDao {
        return db.campaignDao
    }
}


