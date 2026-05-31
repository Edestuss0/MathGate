package com.mathgate.app.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mathgate.app.core.data.campaign.CampaignDao
import com.mathgate.app.core.data.campaign.CampaignEntity
import com.mathgate.app.core.data.education.EducationDao
import com.mathgate.app.core.data.education.EducationEntity
import com.mathgate.app.core.data.lesson_tasks.LessonTaskDao
import com.mathgate.app.core.data.lesson_tasks.LessonTaskEntity
import com.mathgate.app.core.data.lessons.LessonDao
import com.mathgate.app.core.data.lessons.LessonEntity

@Database(
    entities = [CampaignEntity::class, EducationEntity::class, LessonEntity::class, LessonTaskEntity::class],
    version = 8,
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract val campaignDao: CampaignDao
    abstract val educationDao: EducationDao
    abstract val lessonDao: LessonDao
    abstract val lessonTaskDao: LessonTaskDao

}