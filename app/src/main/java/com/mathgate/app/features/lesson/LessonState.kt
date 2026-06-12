package com.mathgate.app.features.lesson

import com.mathgate.app.core.entities.Lesson
import com.mathgate.app.core.entities.LessonPage
import com.mathgate.app.ui.components.AppSnackbarVisuals

data class LessonState(
    val lesson: Lesson? = null,
    val isLoading: Boolean = false,
    val currentPage: LessonPage? = null,
    val isLastPage: Boolean = false,
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null,
    val isPageLocked: Boolean = false
)
