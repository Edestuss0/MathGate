package com.mathgate.app.features.lesson_tasks

data class LessonTasksState(
    val isError: Boolean = false,
    val message: String? = null,
    val completed: Boolean = false,
)