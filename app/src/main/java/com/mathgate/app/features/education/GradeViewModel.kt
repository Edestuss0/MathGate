package com.mathgate.app.features.education

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun onGradeClick(grade: Int) {
        viewModelScope.launch {
            userRepository.changeCurrentGrade(grade)
        }
    }
}