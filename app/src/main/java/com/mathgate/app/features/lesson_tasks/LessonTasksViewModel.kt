package com.mathgate.app.features.lesson_tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.education.EducationRepository
import com.mathgate.app.core.data.lesson_tasks.LessonTaskEntity
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonTasksViewModel @Inject constructor(
    educationRepository: EducationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<LessonTaskEntity>?>(null)
    private val _state = MutableStateFlow(LessonTasksState())
    val state = _state.asStateFlow()
    private val _currentTask = MutableStateFlow<LessonTaskEntity?>(null)
    val currentTask = _currentTask.asStateFlow()
    private var _currentIndex = 0
    private fun isLastTask(): Boolean {
        return _currentIndex >= (_tasks.value?.lastIndex ?: -1)
    }
    private val lessonId = savedStateHandle["id"] ?: 1

    init {
        viewModelScope.launch {
            _tasks.update { null }
            val gettedTasks = educationRepository.getTasksByLessonId(lessonId)
            _tasks.update { gettedTasks }
            _currentTask.update { gettedTasks?.get(_currentIndex) }
            _state.update { it.copy(snackbarMessage = null, isError = false, completed = false) }
        }
    }

    fun onAnswer(answer: String) {
        println("TASKS SIZE: ${_tasks.value?.size}")
        if (currentTask.value?.answer?.contains(answer.trim()) == true) {
            if (isLastTask()) {
                _state.update { it.copy(
                    isError = false,
                    snackbarMessage = AppSnackbarVisuals(
                        message = "Вы прошли практику по уроку",
                        type = SnackbarMessageType.SUCCESS
                    ),
                    completed = true
                )}
            } else {
                _currentIndex++
                _currentTask.update {
                    _tasks.value?.getOrNull(_currentIndex)
                }
                _state.update { it.copy(
                    isError = false,
                    snackbarMessage = AppSnackbarVisuals(
                        message = "Правильно!",
                        type = SnackbarMessageType.SUCCESS
                    ),
                    completed = false
                )}
            }
        } else {
            _state.update { it.copy(
                isError = true,
                snackbarMessage = AppSnackbarVisuals(
                    message = "Неравильно",
                    type = SnackbarMessageType.ERROR
                ),
                completed = false
            )}
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}