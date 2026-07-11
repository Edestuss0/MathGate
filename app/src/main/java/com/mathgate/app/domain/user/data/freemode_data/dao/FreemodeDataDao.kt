package com.mathgate.app.domain.user.data.freemode_data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mathgate.app.domain.user.data.freemode_data.entity.FreemodeDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FreemodeDataDao {

    @Query("SELECT * FROM freemode_data")
    fun getData(): Flow<List<FreemodeDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: FreemodeDataEntity)

    @Query("DELETE FROM freemode_data")
    suspend fun clearAll()
}