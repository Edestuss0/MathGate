package com.mathgate.app.features.oge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.oge.OgeParser
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OgeViewModel @Inject constructor(
    private val parser: OgeParser
) : ViewModel() {

    private val _state = MutableStateFlow<OgeState>(OgeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val question = parser.parseOge()
                _state.update { it.copy(
                    question = question,
                    isError = false
                )}
            } catch (_: Exception) {
                _state.update { it.copy(
                    question = null,
                    isError = true,
                    snackbarMessage = AppSnackbarVisuals(
                        message = "Ошибка при получении вопроса",
                        type = SnackbarMessageType.ERROR
                    )
                )}
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getNewQuestion() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, question = null) }
            try {
                val question = parser.parseOge()
                _state.update { it.copy(
                    question = question,
                    isError = false
                )}
            } catch (_: Exception) {
                _state.update { it.copy(
                    question = null,
                    isError = true,
                    snackbarMessage = AppSnackbarVisuals(
                        message = "Ошибка при получении вопроса",
                        type = SnackbarMessageType.ERROR
                    )
                )}
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAnswer(answer: String) {
        Log.d("OGE_INPUT", answer.trim())
        Log.d("OGE_ANSWER", state.value.question?.answer ?: "")
        if (answer.trim() == state.value.question?.answer) {
            _state.update { it.copy(
                isError = false,
                snackbarMessage = AppSnackbarVisuals(
                    message = "Правильно!",
                    type = SnackbarMessageType.SUCCESS
                )
            )}
            getNewQuestion()
        } else {
            _state.update { it.copy(
                isError = true,
                snackbarMessage = AppSnackbarVisuals(
                    message = "Неправильно",
                    type = SnackbarMessageType.ERROR
                )
            )}
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}