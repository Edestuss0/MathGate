package com.mathgate.app.features.education

import com.mathgate.app.core.entities.EducationTheme
import com.mathgate.app.core.entities.LessonByTheme
import java.util.Collections.emptyList

data class ThemeState(
   val isLoading: Boolean = false,
   val lessons: List<LessonByTheme> = emptyList(),
   val themes: List<EducationTheme> = emptyList(),
   val lessonInfoData: LessonInfoData = LessonInfoData(),
   val themesExpanded: Boolean = false,
   val currentTheme: EducationTheme? = null
)

data class LessonInfoData(
    val show: Boolean = false,
    val lesson: LessonByTheme? = null
)