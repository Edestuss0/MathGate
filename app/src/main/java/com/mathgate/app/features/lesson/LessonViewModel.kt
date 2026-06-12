package com.mathgate.app.features.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.education.EducationRepository
import com.mathgate.app.core.entities.LessonBlockType
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _lessonId = savedStateHandle["id"] ?: 1
    private val _state  = MutableStateFlow(LessonState())
    val state = _state.asStateFlow()
    var currentIndex = 0

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val gettedLesson = educationRepository.getLessonById(_lessonId)
            _state.update { it.copy(
                lesson = gettedLesson,
                currentPage = gettedLesson?.pages?.getOrNull(currentIndex),
                isLoading = false,
                isPageLocked = gettedLesson?.pages?.getOrNull(currentIndex)?.blocks?.any{it.blockType == LessonBlockType.INPUT_QUESTION || it.blockType == LessonBlockType.CHOICE_QUESTION} ?: false
            )}
            if (state.value.lesson?.pages?.size ?: 1 <= currentIndex + 1) {
                _state.update { it.copy(isLastPage = true) }
            }
        }
    }

    fun nextPage() {
        currentIndex += 1
        val page = state.value.lesson?.pages?.getOrNull(currentIndex)
        _state.update { it.copy(
            currentPage = page,
            isPageLocked = page?.blocks?.any{it.blockType == LessonBlockType.INPUT_QUESTION || it.blockType == LessonBlockType.CHOICE_QUESTION} ?: false
        )}
        if (state.value.lesson?.pages?.size ?: 1 <= currentIndex + 1) {
            _state.update { it.copy(isLastPage = true, isError = false) }
        }
    }

    fun onAnswer(answerRightness: Boolean) {
        if (answerRightness) {
            _state.update { it.copy(
                snackbarMessage = AppSnackbarVisuals(
                    type = SnackbarMessageType.SUCCESS,
                    message = "Правильно"
                ),
                isPageLocked = false,
                isError = false
            )}
            if (!_state.value.isLastPage) {
                nextPage()
            }
        } else {
            _state.update { it.copy(
                snackbarMessage = AppSnackbarVisuals(
                    type = SnackbarMessageType.ERROR,
                    message = "Неправильно"
                ),
                isError = true
            )}
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}