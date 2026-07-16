package com.mathgate.app.features.trigonometry.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.features.trigonometry.domain.entity.trigonometryTable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class TrigonometryViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrigonometryState())
    val state = _state.asStateFlow()
    private val _effects = Channel<TrigonometryEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: TrigonometryEvent) {
        when (event) {
            is TrigonometryEvent.SelectAngle -> {
                val angle = event.angle.roundToInt()

                _state.update {
                    it.copy(
                        currentAngle = CurrentAngle(angle),
                        currentTableAngle = trigonometryTable.firstOrNull {
                            it.degree == angle
                        }
                    )
                }
            }
            is TrigonometryEvent.OnCalculate -> {
                _state.update { it.copy(isCalculated = true) }
            }
            is TrigonometryEvent.ToCircle -> {
                _state.update { it.copy(isCalculated = false) }
            }
            is TrigonometryEvent.BackClick -> {
                viewModelScope.launch {
                    _effects.send(TrigonometryEffect.NavigateBack)
                }
            }
        }
    }
}