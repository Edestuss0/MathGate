package com.mathgate.app.core.data.education

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "education")
data class EducationEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
)