package com.mathgate.app.core.data.campaign

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaign")
data class CampaignEntity(
    @PrimaryKey
    val id: Int,
    val question: String,
    val answer: Int,
    val theme: Int,
    val solution: String
)