package com.mathgate.app.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.domain.user.repository.IUserRepository
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
    private val userRepository: IUserRepository
    ) : ViewModel() {

    val authState: StateFlow<AuthState> = userRepository.getUser().map {
        if (it.registered) AuthState.Registered else AuthState.NotRegistered
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = AuthState.Loading
    )
}