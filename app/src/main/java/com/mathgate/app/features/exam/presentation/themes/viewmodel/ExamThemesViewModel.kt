package com.mathgate.app.features.exam.presentation.themes.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.features.exam.domain.usecases.GetExamThemesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamThemesViewModel @Inject constructor(
    private val getThemes: GetExamThemesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val type = ExamTypes.valueOf(savedStateHandle.get<String>("type") ?: "ege")
    private val _effects = Channel<ExamThemesEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()
    private val _state = MutableStateFlow(ExamThemesState())
    val state = _state.asStateFlow()

    fun onEvent(event: ExamThemesEvent) {
        when (event) {
            is ExamThemesEvent.OnBackClick -> {
                viewModelScope.launch {
                    _effects.send(ExamThemesEffect.BackNavigate)
                }
            }
            is ExamThemesEvent.OnPlayClick -> {
                viewModelScope.launch {
                    _effects.send(ExamThemesEffect.PlayNavigate(type.name, event.number))
                }
            }
        }
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            getThemes(type).collect { result ->
                when (result) {
                    is AppResult.Loading -> {_state.update { it.copy(isLoading = true) }}
                    is AppResult.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        _effects.send(ExamThemesEffect.ErrorSnackbar(result.message))
                    }
                    is AppResult.Success -> {
                        _state.update { it.copy(isLoading = false, themes = result.data) }
                    }
                }
            }
        }
    }
}