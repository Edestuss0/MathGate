package com.mathgate.app.features.lessons

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.education.EducationRepository
import com.mathgate.app.core.data.lessons.LessonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _lessons = MutableStateFlow<List<LessonEntity>?>(null)
    val lessons = _lessons.asStateFlow()
    private val _educationId: Int = savedStateHandle["id"] ?: 1

    init {
        viewModelScope.launch {
            _lessons.emit(null)
            val gettedLessons = educationRepository.getLessonByEducationId(_educationId)
            _lessons.emit(gettedLessons)
        }
    }
}
