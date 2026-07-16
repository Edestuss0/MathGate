package com.mathgate.app.features.freemode.presentation.play.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.features.freemode.domain.usecases.GetFreemodeQuestionUseCase
import com.mathgate.app.features.freemode.domain.usecases.OnFreemodeQuestionAnswerUseCase
import com.mathgate.app.shared.user.domain.usecases.CompleteFreemodeUseCase
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
class FreemodeViewModel @Inject constructor(
    private val getQuestion: GetFreemodeQuestionUseCase,
    private val onAnswerAnalytics: OnFreemodeQuestionAnswerUseCase,
    private val getUser: GetUserUseCase,
    private val completeFreemode: CompleteFreemodeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val difficulty = FreemodeDifficulty.valueOf(savedStateHandle.get("difficulty") ?: "easy")
    private val _state = MutableStateFlow(FreemodeState())
    val state = _state.asStateFlow()

    private val _effects = Channel<FreemodeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    val user: StateFlow<User?> = getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        loadData()
    }

    fun onEvent(event: FreemodeEvent) {
        when (event) {
            is FreemodeEvent.OnAnswerInput -> {_state.update { it.copy(answerInput = event.input) }}
            is FreemodeEvent.OnAnswer -> onAnswer()
            is FreemodeEvent.OnBackClick -> {
                viewModelScope.launch {
                    _effects.send(FreemodeEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                _state.update { it.copy(question = getQuestion(difficulty)) }
            } catch (_: Exception) {
                _effects.send(FreemodeEffect.ErrorSnackbar("Ошибка при получении данных"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onAnswer() {
        viewModelScope.launch {
            val isCorrect: Boolean = state.value.answerInput == state.value.question?.answer

            if (state.value.question != null) {
                onAnswerAnalytics(state.value.question!!, difficulty, isCorrect)
                completeFreemode(isCorrect, state.value.question!!, difficulty)
            }
            if (isCorrect) {
                _effects.send(FreemodeEffect.SuccessSnackbar("Правильно"))
                loadData()
            } else {
                _state.update {
                    it.copy(
                        isError = true,
                    )
                }
                _effects.send(FreemodeEffect.ErrorSnackbar("Неправильно"))
            }
        }
    }
}