package com.mathgate.app.features.exam.presentation.home.viewmodel

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
class ExamHomeViewModel @Inject constructor(
    private val getUser: GetUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ExamHomeState())
    val state = _state.asStateFlow()
    private val _effects = Channel<ExamHomeEffects>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val user: StateFlow<User?> = getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun onEvent(event: ExamHomeEvent) {
        when (event) {
            is ExamHomeEvent.OnPlayClick -> {
                viewModelScope.launch {
                    _effects.send(ExamHomeEffects.OnPlayNavigate(state.value.selectedType.name))
                }
            }
            is ExamHomeEvent.OnThemesClick -> {
                viewModelScope.launch {
                    _effects.send(ExamHomeEffects.OnThemesNavigate(state.value.selectedType.name))
                }
            }
            is ExamHomeEvent.OnTypeSelect -> {_state.update { it.copy(selectedType = event.type) }}
        }
    }
}