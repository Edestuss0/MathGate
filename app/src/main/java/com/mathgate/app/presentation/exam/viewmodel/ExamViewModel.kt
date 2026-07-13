package com.mathgate.app.presentation.exam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.features.exam.domain.entity.ExamTypes
import com.mathgate.app.features.exam.usecases.GetExamQuestionByNumberUseCase
import com.mathgate.app.features.exam.usecases.GetExamQuestionUseCase
import com.mathgate.app.features.exam.usecases.GetExamThemesUseCase
import com.mathgate.app.features.exam.usecases.OnAnswerExamQuestionUseCase
import com.mathgate.app.features.user.domain.entity.User
import com.mathgate.app.features.user.domain.repository.IUserRepository
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
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
class ExamViewModel @Inject constructor(
    private val getQuestion: GetExamQuestionUseCase,
    private val getQuestionByNumber: GetExamQuestionByNumberUseCase,
    private val getThemes: GetExamThemesUseCase,
    private val userRepository: IUserRepository,
    private val onAnswerAnalytics: OnAnswerExamQuestionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExamState())
    val state = _state.asStateFlow()
    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    val user: StateFlow<User?> = userRepository.getUser().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = null
    )

    init {
        getNewQuestion()
    }

    fun loadThemes() {
        viewModelScope.launch {
            if (state.value.type == null) {
                _events.send( AppSnackbarVisuals(
                    message = "Некорректный тип экзамена",
                    type = SnackbarMessageType.ERROR
                ))
                return@launch
            }
            _state.update { it.copy(isAnswered = false, isLoading = true, question = null) }
            try {
                getThemes(state.value.type!!).collect { result ->
                    when {
                        result is AppResult.Success -> {
                            _state.update { it.copy(themes = result.data, isError = false) }
                        }
                        result is AppResult.Error -> {
                            _state.update { it.copy(themes = result.data, isError = true) }
                            _events.send(AppSnackbarVisuals(
                                type = SnackbarMessageType.ERROR,
                                message = result.message
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
                    message = "Ошибка при получении темы",
                    type = SnackbarMessageType.ERROR
                ))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getNewQuestion(number: Int? = null) {
        viewModelScope.launch {
            if (state.value.type == null) {
                _events.send( AppSnackbarVisuals(
                    message = "Некорректный тип экзамена",
                    type = SnackbarMessageType.ERROR
                ))
                return@launch
            }
            _state.update { it.copy(isAnswered = false, isLoading = true, question = null) }
            try {
                if (number == null) {
                    getQuestion(state.value.type!!)
                } else {
                    getQuestionByNumber(state.value.type!!, number)
                }.collect { result ->
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
            val isCorrect = answer.trim() == state.value.question?.answer
            if (state.value.question != null && state.value.type != null) {
                onAnswerAnalytics(state.value.question!!, state.value.type!!, isCorrect)
            }
            if (isCorrect) {
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

    fun loadType(newtype: ExamTypes) {
        _state.update { it.copy(type = newtype) }
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