package com.mathgate.app.core.data.theme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class ThemeEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val grade: Int
)