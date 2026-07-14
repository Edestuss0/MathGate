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
    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    fun register(name: String) {
        viewModelScope.launch {
            _state.update { it.copy(isError = false) }
            if (name.length > 20) {
                _state.update { it.copy(isError = true) }
                _events.send(AppSnackbarVisuals(
                    message = "Введите более короткое имя",
                    type = SnackbarMessageType.ERROR
                ))
                return@launch
            }
            try {
                registerUseCase(name)
                _state.update { it.copy(
                    isError = false
                ) }
                _events.send(AppSnackbarVisuals(
                    message = "Регистрация успешна",
                    type = SnackbarMessageType.SUCCESS
                ),)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isError = true
                ) }
                _events.send(AppSnackbarVisuals(
                    message = "Ошибка регистрации",
                    type = SnackbarMessageType.ERROR
                ),)
            }
        }
    }
}