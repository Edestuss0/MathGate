package com.mathgate.app.core.data.education

import android.content.Context
import com.mathgate.app.core.data.lesson_tasks.LessonTaskDao
import com.mathgate.app.core.data.lesson_tasks.LessonTaskEntity
import com.mathgate.app.core.data.lessons.LessonDao
import com.mathgate.app.core.data.lessons.LessonEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import javax.inject.Inject

class EducationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val educationDao: EducationDao,
    private val lessonDao: LessonDao,
    private val lessonTaskDao: LessonTaskDao
) {
    val allEducations = educationDao.getEducation()

    suspend fun getCount(): Int {
        return educationDao.getCount()
    }

    suspend fun getEducationById(id: Int): EducationEntity? {
        return educationDao.getEducationById(id) ?: null
    }

    suspend fun getLessonByEducationId(id: Int): List<LessonEntity>? {
        return lessonDao.getLessonsByEducationId(id) ?: null
    }

    suspend fun checkAndPreloadDb() {
        try {
            val jsonString = context.assets.open("education/education.json")
                .bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val educations = mutableListOf<EducationEntity>()
            val lessons = mutableListOf<LessonEntity>()
            val tasks  = mutableListOf<LessonTaskEntity>()

            for (i in 0 until jsonArray.length()) {
                val themeJson = jsonArray.getJSONObject(i)
                val lessonsJson = themeJson.getJSONArray("lessons")

                for (k in 0 until lessonsJson.length()) {
                    val lessonJson = lessonsJson.getJSONObject(k)
                    val tasksJson = lessonJson.getJSONArray("tasks")

                    lessons.add(
                        LessonEntity(
                            id = lessonJson.getInt("id"),
                            name = lessonJson.getString("lessonname"),
                            material = lessonJson.getString("material"),
                            educationId = themeJson.getInt("id")
                        )
                    )

                    for (j in 0 until tasksJson.length()) {
                        val taskJson = tasksJson.getJSONObject(j)

                        tasks.add(
                            LessonTaskEntity(
                                question = taskJson.getString("question"),
                                answer = taskJson.getJSONArray("answer")
                                    .let { answers ->
                                        List(answers.length()) { index ->
                                            answers.getString(index)
                                        }
                                    },
                                lessonId = lessonJson.getInt("id")
                            )
                        )
                    }
                }

                educations.add(
                    EducationEntity(
                        id = themeJson.optInt("id", i + 1),
                        name = themeJson.getString("themename"),
                    )
                )
            }

            educationDao.insertEducations(educations)
            lessonDao.insertLessons(lessons)
            lessonTaskDao.insertLessonTasks(tasks)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}