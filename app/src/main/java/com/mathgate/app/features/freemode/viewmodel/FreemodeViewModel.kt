package com.mathgate.app.features.freemode.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.GenerateMath
import com.mathgate.app.core.data.user.UserRepository
import com.mathgate.app.core.entities.User
import com.mathgate.app.domain.exam.repository.ExamRepository
import com.mathgate.app.domain.freemode.entity.FreemodeDifficulty
import com.mathgate.app.domain.freemode.repository.IFreemodeRepository
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class FreemodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val freemodeRepository: IFreemodeRepository
) : ViewModel() {
    private val _difficulty = savedStateHandle["difficulty"] ?: "easy"
    private val _state = MutableStateFlow(FreemodeState())
    val state = _state.asStateFlow()

    private val _events = Channel<AppSnackbarVisuals>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _user = userRepository.getProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = User()
    )

    init {
        _state.update { it.copy(question = freemodeRepository.getQuestion(FreemodeDifficulty.valueOf(_difficulty.uppercase()))) }
    }

    fun onInputChange(newValue: String) {
        _state.update { it.copy(answerInput = newValue) }
    }

    fun onAnswer() {
        val isCorrect: Boolean = state.value.answerInput == state.value.question?.answer

        if (isCorrect) {
            _state.update {
                it.copy(
                    question = freemodeRepository.getQuestion(FreemodeDifficulty.valueOf(_difficulty.uppercase())),
                    answerInput = "",
                    streak = it.streak + 1,
                    isError = false,
                )
            }
            viewModelScope.launch {
                _events.send(AppSnackbarVisuals(
                    message = "Правильно!",
                    type = SnackbarMessageType.SUCCESS
                ))
            }
            val streak: Int = if (_state.value.streak > 0) { _state.value.streak } else { 1 }
            val points = when (_difficulty) {"easy" -> 5 "medium" -> 15 "hard" -> 40 else -> 15}
            viewModelScope.launch {
                userRepository.addExperience((points + (streak * 0.1)).roundToInt())
                if (streak > _user.value.best_streak) {
                    userRepository.updateStreak(streak)
                }
            }
        } else {
            _state.update {
                it.copy(
                    streak = 0,
                    isError = true,
                )
            }
            viewModelScope.launch {
                _events.send( AppSnackbarVisuals(
                    message = "Неправильно",
                    type = SnackbarMessageType.ERROR
                ))
            }
        }

    }
}