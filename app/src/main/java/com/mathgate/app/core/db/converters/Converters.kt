package com.mathgate.app.core.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mathgate.app.features.exam.domain.entity.ExamTypes

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        if (json.isNullOrBlank()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromType(type: ExamTypes): String {
        return type.name
    }
    @TypeConverter
    fun toType(type: String): ExamTypes {
        return ExamTypes.valueOf(type)
    }
}