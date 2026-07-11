package com.mathgate.app.features.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.education.EducationRepository
import com.mathgate.app.core.entities.EducationTheme
import com.mathgate.app.core.entities.LessonByTheme
import com.mathgate.app.domain.user.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val educationRepository: EducationRepository,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ThemeState>(ThemeState())
    val state = _state.asStateFlow()

    fun getInitialData(grade: Int) {
        viewModelScope.launch {
            if (grade < 1) {
//                userRepository.removeCurrentGrade()
                return@launch
            }
            try {
                _state.update { it.copy(isLoading = true) }
                val themes = educationRepository.getThemesByGrade(grade)
                _state.update { it.copy(themes = themes, currentTheme = themes.firstOrNull()) }
                val firstTheme = themes.firstOrNull()
                if (firstTheme != null) {
                    val lessons = educationRepository.getLessonsByTheme(firstTheme.id)
                    _state.update {
                        it.copy(
                            themes = themes,
                            currentTheme = firstTheme,
                            lessons = lessons,
                            isLoading = false
                        )
                    }
                } else {
//                    userRepository.removeCurrentGrade()
                    _state.update { it.copy(isLoading = false) }
                }
            } catch (_: Exception) {
//                userRepository.removeCurrentGrade()
                _state.update { it.copy(isLoading = false, currentTheme = null, lessons = emptyList()) }
            }
        }
    }

    fun showThemes() {
        _state.update { it.copy(themesExpanded = true) }
    }

    fun hideThemes() {
        _state.update { it.copy(themesExpanded = false) }
    }

    fun showLessonInfo(lesson: LessonByTheme) {
        _state.update { it.copy(
            lessonInfoData = LessonInfoData(
                show = true,
                lesson = lesson
            )
        ) }
    }


    fun hideLessonInfo() {
        _state.update { it.copy(
            lessonInfoData = LessonInfoData(
                show = false,
                lesson = null
            )
        )}
    }

    fun changeCurrentTheme(theme: EducationTheme) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val lessons = educationRepository.getLessonsByTheme(theme.id)
            _state.update { it.copy(currentTheme = theme, lessons = lessons, isLoading = false) }
        }
    }

    fun onGradeClick() {
        viewModelScope.launch {
//            userRepository.removeCurrentGrade()
        }
    }
}