package com.mathgate.app.core.data.education

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationDao {

    @Query("SELECT * FROM education")
    fun getEducation(): Flow<List<EducationEntity>>

    @Query("SELECT * FROM education WHERE id = :id LIMIT 1")
    suspend fun getEducationById(id: Int): EducationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducations(educations: List<EducationEntity>)

    @Query("SELECT COUNT(*) FROM education")
    suspend fun getCount(): Int
}