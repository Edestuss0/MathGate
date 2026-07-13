package com.mathgate.app.presentation.freemode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.features.user.domain.entity.User
import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.repository.IFreemodeRepository
import com.mathgate.app.features.freemode.usecases.GetFreemodeQuestionUseCase
import com.mathgate.app.features.freemode.usecases.OnFreemodeQuestionAnswerUseCase
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
class FreemodeViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val getQuestion: GetFreemodeQuestionUseCase,
    private val onAnswerAnalytics: OnFreemodeQuestionAnswerUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FreemodeState())
    val state = _state.asStateFlow()

    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    val user: StateFlow<User?> = userRepository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                _state.update { it.copy(question = getQuestion(state.value.selectedDifficulty)) }
            } catch (e: Exception) {
                _events.send(AppSnackbarVisuals(
                    type = SnackbarMessageType.ERROR,
                    message = "Ошибка при получении данных"
                ))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onInputChange(newValue: String) {
        _state.update { it.copy(answerInput = newValue) }
    }

    fun onAnswer() {
        val isCorrect: Boolean = state.value.answerInput == state.value.question?.answer
        if (state.value.question != null) {
            onAnswerAnalytics(state.value.question!!, state.value.selectedDifficulty, isCorrect)
        }

        if (isCorrect) {
            viewModelScope.launch {
                _events.send(AppSnackbarVisuals(
                    message = "Правильно!",
                    type = SnackbarMessageType.SUCCESS
                ))
                userRepository.completeFreemode(isCorrect, state.value.question!!)
                loadData()
            }
        } else {
            _state.update {
                it.copy(
                    isError = true,
                )
            }
            viewModelScope.launch {
                userRepository.completeFreemode(isCorrect, state.value.question!!)
                _events.send( AppSnackbarVisuals(
                    message = "Неправильно",
                    type = SnackbarMessageType.ERROR
                ))
            }
        }

    }

    fun selectDifficulty(difficulty: FreemodeDifficulty) {
        _state.update { it.copy(selectedDifficulty = difficulty) }
    }
}