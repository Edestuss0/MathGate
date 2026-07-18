package com.mathgate.app.core.db.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mathgate.app.core.db.converters.Converters
import com.mathgate.app.features.exam.data.local.questions.dao.ExamQuestionCacheDao
import com.mathgate.app.features.exam.data.local.questions.entity.ExamQuestionCacheEntity
import com.mathgate.app.features.exam.data.local.themes.dao.ExamThemeCacheDao
import com.mathgate.app.features.exam.data.local.themes.entity.ExamThemeCacheEntity
import com.mathgate.app.features.user.data.exam_data.dao.ExamDataDao
import com.mathgate.app.features.user.data.exam_data.entity.ExamDataEntity
import com.mathgate.app.features.user.data.freemode_data.dao.FreemodeDataDao
import com.mathgate.app.features.user.data.freemode_data.entity.FreemodeDataEntity

@Database(
    entities = [FreemodeDataEntity::class, ExamDataEntity::class,],
    version = 15,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UserDb : RoomDatabase() {
    abstract val examDataDao: ExamDataDao
    abstract val freemodeDataDao: FreemodeDataDao

}