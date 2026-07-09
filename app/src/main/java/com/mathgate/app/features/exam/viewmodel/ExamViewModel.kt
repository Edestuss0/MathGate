package com.mathgate.app.features.exam.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.oge.OgeParser
import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.domain.exam.entity.ExamTypes
import com.mathgate.app.domain.exam.usecases.GetExamQuestionUseCase
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
class ExamViewModel @Inject constructor(
    private val getQuestion: GetExamQuestionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExamState())
    val state = _state.asStateFlow()
    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    private var type: ExamTypes? = null

    init {
        getNewQuestion()
    }

    fun getNewQuestion() {

        viewModelScope.launch {
            if (type == null) {
                _events.send( AppSnackbarVisuals(
                    message = "Некорректный тип экзамена",
                    type = SnackbarMessageType.ERROR
                ))
                return@launch
            }
            _state.update { it.copy(isAnswered = false, isLoading = true, question = null) }
            try {
                getQuestion(type!!).collect { result ->
                    when {
                        result is AppResult.Success -> {
                            _state.update { it.copy(
                                question = result.data,
                                isError = false,
                                isLoading = false
                            )}
                        }
                        result is AppResult.Error -> {
                            _state.update { it.copy(
                                question = result.data,
                                isError = true,
                                isLoading = false
                            )}
                            _events.send( AppSnackbarVisuals(
                                message = result.message,
                                type = SnackbarMessageType.ERROR,
                            ))
                        }
                        result is AppResult.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }
                    }
                }
            } catch (_: Exception) {
                _state.update { it.copy(
                    question = null,
                    isError = true,
                )}
                _events.send( AppSnackbarVisuals(
                    message = "Ошибка при получении вопроса",
                    type = SnackbarMessageType.ERROR
                ))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAnswer(answer: String) {
        viewModelScope.launch {
            Log.d("OGE_INPUT", answer.trim())
            Log.d("OGE_ANSWER", state.value.question?.answer ?: "")
            if (answer.trim() == state.value.question?.answer) {
                _state.update { it.copy(
                    isError = false,
                )}
                _events.send(AppSnackbarVisuals(
                    message = "Правильно!",
                    type = SnackbarMessageType.SUCCESS
                ))
                getNewQuestion()
            } else {
                _state.update { it.copy(
                    isError = true,
                )}
                _events.send(AppSnackbarVisuals(
                    message = "Неправильно",
                    type = SnackbarMessageType.ERROR
                ))
            }
        }
    }

    fun load(Newtype: ExamTypes) {
        type = Newtype
        getNewQuestion()
    }

    fun skipQuestion() {
        viewModelScope.launch {
            _state.update { it.copy(
                isAnswered = true
            )}
            _events.send(AppSnackbarVisuals(
                message = "Вы пропустили вопрос",
                type = SnackbarMessageType.ERROR
            ))
        }
    }
}