package com.mathgate.app.core.data.education

import android.util.Log
import com.mathgate.app.core.api.ApiClient
import com.mathgate.app.core.data.lesson_blocks.LessonBlockEntity
import com.mathgate.app.core.data.lesson_blocks.LessonBlocksDao
import com.mathgate.app.core.data.lesson_pages.LessonPageDao
import com.mathgate.app.core.data.lesson_pages.LessonPageEntity
import com.mathgate.app.core.data.lessons.LessonDao
import com.mathgate.app.core.data.lessons.LessonEntity
import com.mathgate.app.core.data.theme.ThemeDao
import com.mathgate.app.core.data.theme.ThemeEntity
import com.mathgate.app.core.entities.DownloadedTheme
import com.mathgate.app.core.entities.EducationTheme
import com.mathgate.app.core.entities.Lesson
import com.mathgate.app.core.entities.LessonByTheme
import com.mathgate.app.core.entities.LessonDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class EducationRepository @Inject constructor(
    private val themeDao: ThemeDao,
    private val lessonDao: LessonDao,
    private val lessonPageDao: LessonPageDao,
    private val lessonBlockDao: LessonBlocksDao
) {
    private val client = ApiClient.client

    private suspend fun downloadThemesByGrade(grade: Int) {
        try {
        val res = client.get("http://151.242.88.125:9090/api/education/themes/grade/${grade}/download")
        if (res.status.value == 200) {
            val downloadedThemes = res.body<List<DownloadedTheme>>()
            val themesToAdd = mutableListOf<ThemeEntity>()
            val lessonsToAdd = mutableListOf<LessonEntity>()
            val pagesToAdd = mutableListOf<LessonPageEntity>()
            val blocksToAdd = mutableListOf<LessonBlockEntity>()

            downloadedThemes.map { theme ->
                theme.lessons.mapIndexed { index, lesson ->
                    lesson.pages.map {page ->
                        page.blocks.map { block ->
                            blocksToAdd.add(
                                LessonBlockEntity(
                                    id = block.id,
                                    orderIndex = block.orderIndex,
                                    blockType = block.blockType.toString(),
                                    pageId = page.id,
                                    payload = block.payload.toString()
                                )
                            )
                        }
                        pagesToAdd.add(
                            LessonPageEntity(
                                id = page.id,
                                orderIndex = page.orderIndex,
                                lessonId = lesson.id
                            )
                        )
                    }
                    lessonsToAdd.add(
                        LessonEntity(
                            id = lesson.id,
                            title = lesson.title,
                            description = lesson.description,
                            themeId = lesson.themeId,
                            orderIndex = index
                        )
                    )
                }

                themesToAdd.add(
                    ThemeEntity(
                        id = theme.id,
                        name = theme.name,
                        grade = theme.grade,
                    )
                )
            }
            themeDao.insertThemes(themesToAdd)
            lessonDao.insertLessons(lessonsToAdd)
            lessonPageDao.insertLessonPages(pagesToAdd)
            lessonBlockDao.insertLessonBlocks(blocksToAdd)
        }
        } catch (e: Exception) { }
    }

    suspend fun getThemesByGrade(grade: Int): List<EducationTheme> {
        try {
            val res = client.get("http://151.242.88.125:9090/api/education/themes/grade/${grade}")
            downloadThemesByGrade(grade)
            if (res.status.value == 200) {
                return res.body<List<EducationTheme>>()
            }
        } catch (e: Exception) {
            val themes = themeDao.getThemesByGrade(grade).map {
                EducationTheme(
                    id = it.id,
                    name = it.name,
                    grade = it.grade
                )
            }
            return themes
        }
        return emptyList()
    }

    suspend fun getLessonsByTheme(id: Int): List<LessonByTheme> {
        try {
        val res = client.get("http://151.242.88.125:9090/api/education/theme/${id}/lessons")
            if (res.status.value == 200) {
                return res.body<List<LessonByTheme>>()
            }
        } catch (e: Exception) {
            return lessonDao.getLessonsByThemeId(id).map {
                LessonByTheme(
                    id = it.id,
                    title = it.title,
                    orderIndex = it.orderIndex,
                    description = it.description
                )
            }
        }
        return emptyList()
    }

    suspend fun getLessonById(id: Int): Lesson? {
        try {
            val res = client.get("http://151.242.88.125:9090/api/education/lesson/$id")
            if (res.status.value == 200) {
                return res.body<LessonDto>().toLesson()
            }
        } catch(e: Exception) {
            val lessonWithPages = lessonDao.getLessonWithPages(id) ?: return null
            val blocks = lessonBlockDao.getBlocksByLessonId(id)
            return lessonWithPages.toLesson(blocks)
        }
        return null
    }
}