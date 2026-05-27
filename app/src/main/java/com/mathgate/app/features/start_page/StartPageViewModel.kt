package com.mathgate.app.features.start_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StartPageState(
    val isError: Boolean = false,
    val message: String? = null,
)

class StartPageViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val _state = MutableStateFlow(StartPageState())
    val state = _state.asStateFlow()


    fun register(name: String) {
        try {
            viewModelScope.launch {
                userRepository.register(name)
            }
            _state.update { it.copy(message = "Регистрация успешна", isError = false) }
        } catch (e: Exception) {
            _state.update { it.copy(message = "Проверьте введённое имя", isError = true) }
        }
    }
    
    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }

}