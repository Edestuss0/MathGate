package com.mathgate.app.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mathgate.app.core.data.campaign.CampaignDao
import com.mathgate.app.core.data.campaign.CampaignEntity

@Database(
    entities = [CampaignEntity::class],
    version = 1,
)
abstract class MainDb : RoomDatabase() {

    abstract val campaignDao: CampaignDao

    companion object{
        @Volatile
        private var INSTANCE: MainDb? = null

        fun getDatabase(context: Context): MainDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "main.db",
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}