package com.mathgate.app.features.education

import androidx.lifecycle.ViewModel
import com.mathgate.app.domain.user.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {
    fun onGradeClick(grade: Int) {
//        viewModelScope.launch {
//            userRepository.changeCurrentGrade(grade)
//        }
    }
}