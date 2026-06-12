package com.mathgate.app.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class AuthState {
    object Loading : AuthState()
    object Registered : AuthState()
    object NotRegistered : AuthState()
}

@HiltViewModel
class MainViewModel @Inject constructor (
    private val userRepository: UserRepository
    ) : ViewModel() {

    val authState: StateFlow<AuthState> = userRepository.getProfile().map { user ->
        if (user.registered) AuthState.Registered else AuthState.NotRegistered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthState.Loading
    )

    val currentGrade: StateFlow<Int?> = userRepository.getProfile().map { user ->
        user.current_grade
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

}