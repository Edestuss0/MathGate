package com.mathgate.app.features.user.presentation.profile.viewmodel

data class ProfileState(
    val isLoading: Boolean = false
)

sealed class ProfileEvent {
    object OnExamStatsClick : ProfileEvent()
    object OnFreemodeStatsClick : ProfileEvent()
}

sealed class ProfileEffect {
    object ExamStatsNavigate : ProfileEffect()
    object FreemodeStatsNavigate : ProfileEffect()
}
