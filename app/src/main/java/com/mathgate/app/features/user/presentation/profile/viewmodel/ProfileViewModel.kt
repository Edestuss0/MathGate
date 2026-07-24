package com.mathgate.app.features.user.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.features.user.domain.usecases.DeleteAccountUseCase
import com.mathgate.app.shared.user.domain.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUser: GetUserUseCase,
    private val deleteAccount: DeleteAccountUseCase
) : ViewModel() {

    private val _effects = Channel<ProfileEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    val user: StateFlow<User?> = getUser()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnFreemodeStatsClick -> {
                viewModelScope.launch {
                    _effects.send(ProfileEffect.FreemodeStatsNavigate)
                }
            }
            is ProfileEvent.OnExamStatsClick -> {
                viewModelScope.launch {
                    _effects.send(ProfileEffect.ExamStatsNavigate)
                }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            deleteAccount()
        }
    }
}