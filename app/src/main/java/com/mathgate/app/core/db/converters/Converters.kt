package com.mathgate.app.core.db.converters

import androidx.room.TypeConverter
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import kotlinx.serialization.json.Json

class Converters {


    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        if (json.isNullOrBlank()) return emptyList()
        return Json.decodeFromString<List<String>>(json)
    }

    @TypeConverter
    fun fromType(type: ExamTypes): String {
        return type.name
    }
    @TypeConverter
    fun toType(type: String): ExamTypes {
       return runCatching { return ExamTypes.valueOf(type) }.onFailure { return ExamTypes.EGE }.getOrNull() ?: ExamTypes.EGE
    }

    @TypeConverter
    fun fromDifficulty(difficulty: FreemodeDifficulty): String {
        return difficulty.name
    }
    @TypeConverter
    fun toDifficulty(difficulty: String): FreemodeDifficulty {
        return runCatching { return FreemodeDifficulty.valueOf(difficulty) }.onFailure { return FreemodeDifficulty.MEDIUM }.getOrNull() ?: FreemodeDifficulty.MEDIUM
    }
}