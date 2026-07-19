package com.mathgate.app.features.user.presentation.exam_stats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.shared.user.domain.entity.ExamStatsItem
import com.mathgate.app.shared.user.domain.usecases.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExamStatsViewModel @Inject constructor(
    private val getUser: GetUserUseCase
) : ViewModel() {
    val stats: StateFlow<List<ExamStatsItem>?> = getUser().map { it.examData }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
}