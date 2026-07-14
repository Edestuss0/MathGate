package com.mathgate.app.features.freemode.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.user.domain.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreemodeHomeViewmodel @Inject constructor(
    private val getUser: GetUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FreemodeHomeState())
    val state = _state.asStateFlow()
    private val _effects = Channel<FreemodeHomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val user: StateFlow<User?> = getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun onEvent(event: FreemodeHomeEvent) {
        when (event) {
            is FreemodeHomeEvent.PlayClick -> {
                viewModelScope.launch {
                    _effects.send(FreemodeHomeEffect.PlayNavigate(state.value.selectedDifficulty.name))
                }
            }
            is FreemodeHomeEvent.SelectDifficulty -> {
                _state.update { it.copy(selectedDifficulty = event.difficulty) }
            }
        }
    }
}