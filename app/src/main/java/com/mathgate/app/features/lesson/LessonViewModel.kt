package com.mathgate.app.features.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.education.EducationRepository
import com.mathgate.app.core.data.lessons.LessonEntity
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
    private val _lessonById  = MutableStateFlow<LessonEntity?>(null)
    val lessonById = _lessonById.asStateFlow()

    init {
        viewModelScope.launch {
            _lessonById.update { null }
            val gettedLesson = educationRepository.getLessonById(_lessonId)
            _lessonById.update { gettedLesson }
        }
    }
}