package com.mathgate.app.features.user.data.exam_data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mathgate.app.features.user.data.exam_data.entity.ExamDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDataDao {

    @Query("SELECT * FROM exam_data")
    fun getData(): Flow<List<ExamDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ExamDataEntity)

    @Query("DELETE FROM exam_data")
    suspend fun clearAll()
}