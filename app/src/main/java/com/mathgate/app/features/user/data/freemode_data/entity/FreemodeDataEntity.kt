package com.mathgate.app.features.user.data.freemode_data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "freemode_data")
data class FreemodeDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val answer: String,
    val isCorrect: Boolean,
)