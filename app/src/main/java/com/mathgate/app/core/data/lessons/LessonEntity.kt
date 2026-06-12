package com.mathgate.app.core.data.lessons

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mathgate.app.core.data.theme.ThemeEntity

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = ThemeEntity::class,
            parentColumns = ["id"],
            childColumns = ["themeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("themeId")]
)
data class LessonEntity(
    @PrimaryKey
    val id: Int,
    val themeId: Int,
    val title: String,
    val description: String,
    val orderIndex: Int
)