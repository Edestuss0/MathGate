package com.mathgate.app.features.freemode.presentation.home.viewmodel

import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty

data class FreemodeHomeState(
    val selectedDifficulty: FreemodeDifficulty = FreemodeDifficulty.MEDIUM
)

sealed class FreemodeHomeEvent {
    data class SelectDifficulty(val difficulty: FreemodeDifficulty) : FreemodeHomeEvent()
    data object PlayClick : FreemodeHomeEvent()
}

sealed class FreemodeHomeEffect {
    data class PlayNavigate(val difficulty: String) : FreemodeHomeEffect()
}
