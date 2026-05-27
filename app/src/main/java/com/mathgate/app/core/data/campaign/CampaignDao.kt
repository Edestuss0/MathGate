package com.mathgate.app.core.data.campaign

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaign")
    fun getAllCampaigns(): Flow<List<CampaignEntity>>

    @Query("SELECT * FROM campaign WHERE id = :id LIMIT 1")
    suspend fun getCampaignById(id: Int): CampaignEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaigns(campaigns: List<CampaignEntity>)

    @Query("SELECT COUNT(*) FROM campaign")
    suspend fun getCount(): Int
}