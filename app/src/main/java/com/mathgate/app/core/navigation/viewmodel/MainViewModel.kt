package com.mathgate.app.core.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.shared.user.domain.usecases.GetAuthorizedStatusUseCase
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
    private val getAuthStatus: GetAuthorizedStatusUseCase
    ) : ViewModel() {

    val authState: StateFlow<AuthState> = getAuthStatus().map { if (it) AuthState.Registered else AuthState.NotRegistered }
        .stateIn(
            scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = AuthState.Loading
        )
}