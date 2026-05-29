package com.mathgate.app.features.freemode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.GenerateMath
import com.mathgate.app.core.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class FreemodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _difficulty = savedStateHandle["difficulty"] ?: "easy"
    private val _state = MutableStateFlow(FreemodeState())
    val state = _state.asStateFlow()

    private val generator = GenerateMath(_difficulty)

    init {
        _state.update { it.copy(question = generator.question) }
    }

    fun onInputChange(newValue: String) {
        _state.update { it.copy(answerInput = newValue) }
    }

    fun onAnswer() {
        val isCorrect: Boolean = generator.answer(_state.value.answerInput)

        if (isCorrect) {
            _state.update {
                it.copy(
                    question = generator.question,
                    answerInput = "",
                    streak = it.streak + 1,
                    isError = false,
                    message = "Правильно!"
                )
            }
            val streak = if (_state.value.streak > 0) { _state.value.streak } else { 1 }
            val points = when (_difficulty) {"easy" -> 5 "medium" -> 15 "hard" -> 40 else -> 15}
            viewModelScope.launch {
                userRepository.addExperience((points + (streak * 0.1)).roundToInt())
            }
        } else {
            _state.update {
                it.copy(
                    streak = 0,
                    isError = true,
                    message = "Неправильно("
                )
            }
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }
}