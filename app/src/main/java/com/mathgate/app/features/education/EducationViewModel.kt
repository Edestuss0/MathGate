package com.mathgate.app.features.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.education.EducationRepository
import com.mathgate.app.core.data.lessons.LessonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EducationViewModel @Inject constructor(
    private val educationRepository: EducationRepository
) : ViewModel() {

    val allEducations = educationRepository.allEducations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

   private val _lessons = MutableStateFlow<List<LessonEntity>?>(null)
    val lessons = _lessons.asStateFlow()

    fun getLessons(id: Int) {
        viewModelScope.launch {
            _lessons.update { null }
            val gettedLessons = educationRepository.getLessonByEducationId(id)
            _lessons.update { gettedLessons }
        }
    }

    fun initializeData() {
        viewModelScope.launch {
            educationRepository.checkAndPreloadDb()
        }
    }

}