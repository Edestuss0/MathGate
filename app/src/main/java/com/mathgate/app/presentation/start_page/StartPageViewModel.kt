package com.mathgate.app.presentation.start_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.features.user.domain.repository.IUserRepository
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class StartPageViewModel @Inject constructor (
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StartPageState())
    val state = _state.asStateFlow()


    fun register(name: String) {
        viewModelScope.launch {
            try {
                userRepository.register(name)
                _state.update { it.copy(
                    snackbarMessage = AppSnackbarVisuals(
                        message = "Регистрация успешна",
                        type = SnackbarMessageType.SUCCESS
                    ),
                    isError = false
                ) }
            } catch (e: Exception) {
                _state.update { it.copy(
                    snackbarMessage = AppSnackbarVisuals(
                        message = "Ошибка регистрации",
                        type = SnackbarMessageType.ERROR
                    ),
                    isError = true
                ) }
            }
        }
    }
    
    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }

}