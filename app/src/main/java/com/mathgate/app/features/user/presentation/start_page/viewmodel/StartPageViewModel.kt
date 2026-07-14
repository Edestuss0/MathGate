package com.mathgate.app.features.user.presentation.start_page.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.features.user.domain.usecases.RegisterUseCase
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class StartPageViewModel @Inject constructor (
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StartPageState())
    val state = _state.asStateFlow()
    private val _effects = Channel<StartPageEffects>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: StartPageEvent) {
        when (event) {
            is StartPageEvent.Register -> {register()}
            is StartPageEvent.OnInputName -> {_state.update { it.copy(nameInput = event.input) }}
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isError = false) }
            if (state.value.nameInput.length > 20) {
                _state.update { it.copy(isError = true) }
                _effects.send(StartPageEffects.ErrorSnackbar("Введите более короткое имя"))
                return@launch
            }
            try {
                registerUseCase(state.value.nameInput)
                _state.update { it.copy(
                    isError = false
                ) }
                _effects.send(StartPageEffects.SuccessSnackbar("Регистрация успешна"))
            } catch (_: Exception) {
                _state.update { it.copy(
                    isError = true
                ) }
                _effects.send(StartPageEffects.ErrorSnackbar("Ошибка регистрации"))
            }
        }
    }
}