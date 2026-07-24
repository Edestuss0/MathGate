package com.mathgate.app.features.exam.presentation.play.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.features.exam.domain.usecases.GetExamQuestionByNumberUseCase
import com.mathgate.app.features.exam.domain.usecases.GetExamQuestionUseCase
import com.mathgate.app.features.exam.domain.usecases.GetExamThemesUseCase
import com.mathgate.app.features.exam.domain.usecases.OnAnswerExamQuestionUseCase
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.user.domain.usecases.CompleteExamUseCase
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
class ExamViewModel @Inject constructor(
    private val getQuestion: GetExamQuestionUseCase,
    private val getQuestionByNumber: GetExamQuestionByNumberUseCase,
    private val getThemes: GetExamThemesUseCase,
    private val getUser: GetUserUseCase,
    private val completeExam: CompleteExamUseCase,
    private val onAnswerAnalytics: OnAnswerExamQuestionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ExamPlayState())
    val state = _state.asStateFlow()
    private val _effects = Channel<ExamPlayEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val user: StateFlow<User?> = getUser().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = null
    )
    val type = ExamTypes.valueOf(savedStateHandle.get<String>("type") ?: "ege")
    private val number = savedStateHandle.get<Int>("number")

    init {
        getNewQuestion()
    }

    fun onEvent(event: ExamPlayEvent) {
        when (event) {
            is ExamPlayEvent.OnSkip -> skipQuestion()
            is ExamPlayEvent.OnAnswer -> onAnswer()
            is ExamPlayEvent.OnBackClick -> {
                viewModelScope.launch { _effects.send(ExamPlayEffect.NavigateBack) }
            }
            is ExamPlayEvent.OnAnswerInput -> {_state.update { it.copy(answerInput = event.input) }}
            is ExamPlayEvent.OnGetNewQuestion -> getNewQuestion()
        }
    }

    private fun getNewQuestion() {
        viewModelScope.launch {
            _state.update { it.copy(isSkipped = false, isLoading = true, question = null) }
            try {
                if (number == null) {
                    getQuestion(type)
                } else {
                    getQuestionByNumber(type, number)
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
                            _effects.send(ExamPlayEffect.ErrorSnackbar(result.message))
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
                _effects.send(ExamPlayEffect.ErrorSnackbar("Ошибка при получении вопроса"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onAnswer() {
        if (state.value.isAnswered) return
        viewModelScope.launch {
            _state.update { it.copy(isAnswered = true) }
            try {
                val isCorrect = state.value.answerInput.trim() == state.value.question?.answer
                if (state.value.question != null) {
                    completeExam(isCorrect, state.value.question!!, type)
                    onAnswerAnalytics(state.value.question!!, type, isCorrect)
                }
                if (isCorrect) {
                    _state.update { it.copy(
                        isError = false,
                    )}
                    _effects.send(ExamPlayEffect.SuccessSnackbar("Правильно"))
                    getNewQuestion()
                } else {
                    _state.update { it.copy(
                        isError = true,
                    )}
                    _effects.send(ExamPlayEffect.ErrorSnackbar("Неправильно"))
                }
            } finally {
                _state.update { it.copy(isAnswered = false) }
            }
        }
    }

    private fun skipQuestion() {
        viewModelScope.launch {
            _state.update { it.copy(
                isSkipped = true
            )}
            _effects.send(ExamPlayEffect.ErrorSnackbar("Вы пропустили вопрос"))
        }
    }
}