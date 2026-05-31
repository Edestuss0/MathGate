package com.mathgate.app.features.lesson_tasks

import com.mathgate.app.ui.components.AppSnackbarVisuals

data class LessonTasksState(
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null,
    val completed: Boolean = false,
)