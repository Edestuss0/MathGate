package com.mathgate.app.features.freemode.presentation.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.presentation.home.viewmodel.FreemodeHomeEffect
import com.mathgate.app.features.freemode.presentation.home.viewmodel.FreemodeHomeEvent
import com.mathgate.app.features.freemode.presentation.home.viewmodel.FreemodeHomeViewmodel
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreemodeHomeScreen(
    onStartNavigate: (difficulty: String) -> Unit,
    viewmodel: FreemodeHomeViewmodel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewmodel.state.collectAsState()
    val user by viewmodel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect {
            when (it) {
                is FreemodeHomeEffect.PlayNavigate -> {onStartNavigate(it.difficulty)}
            }
        }
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when {
            user == null -> {
                EmptyState("Не удалось получить необходимую информацию")
            }
            else -> {
                FreemodeHomeScreenContent(
                    selectedDifficulty = state.selectedDifficulty,
                    onDifficultySelect = {new -> viewmodel.onEvent(FreemodeHomeEvent.SelectDifficulty(new))},
                    onStartButtonClick = {viewmodel.onEvent(FreemodeHomeEvent.PlayClick)},
                    user =  user!!
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FreemodeHomeScreenContent(
    selectedDifficulty: FreemodeDifficulty,
    onDifficultySelect: (new: FreemodeDifficulty) -> Unit,
    onStartButtonClick: (diff: String) -> Unit,
    user: User
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Свободный режим",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppCard(
                    modifier = Modifier.weight(1f),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null
                            )
                            Text(
                                text = "Серия: ${user.currentStreak}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
                AppCard(
                    modifier = Modifier.weight(1f),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                            Text(
                                text = "Верных: ${user.freemodeSuccessRate}%",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            AppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Решайте математические примеры в свободном режиме",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        FreemodeDifficulty.entries.forEachIndexed { index, difficulty ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = FreemodeDifficulty.entries.size
                                ),
                                onClick = {onDifficultySelect(difficulty)},
                                selected = difficulty == selectedDifficulty,
                                label = {Text(difficulty.label)}
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            PrimaryButton(
                onClick = { onStartButtonClick(selectedDifficulty.name.lowercase()) },
                modifier = Modifier.fillMaxWidth(),
                text = "Начать"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FreemodeHomeContentPreview() {
    FreemodeHomeScreenContent(
        selectedDifficulty = FreemodeDifficulty.MEDIUM,
        onDifficultySelect = {},
        onStartButtonClick = {},
        user = User("Traktoristka", 12, 1000, 54, 24, 24, listOf(true, true, false, true), emptyList(), true)
    )
}