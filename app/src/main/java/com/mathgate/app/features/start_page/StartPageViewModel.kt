package com.mathgate.app.features.start_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StartPageState(
    val isError: Boolean = false,
    val message: String? = null,
)

@HiltViewModel
class StartPageViewModel @Inject constructor (
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StartPageState())
    val state = _state.asStateFlow()


    fun register(name: String) {
        viewModelScope.launch {
            try {
                userRepository.register(name)
                _state.update { it.copy(message = "Регистрация успешна", isError = false) }
            } catch (e: Exception) {
                _state.update { it.copy(message = "Проверьте введённое имя", isError = true) }
            }
        }
    }
    
    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }

}