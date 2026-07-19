package com.mathgate.app.features.freemode.presentation.play.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularAlt1Bar
import androidx.compose.material.icons.filled.SignalCellularAlt2Bar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import com.mathgate.app.features.freemode.domain.entity.FreemodeBlockType
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestionBlock
import com.mathgate.app.features.freemode.presentation.play.viewmodel.FreemodeEffect
import com.mathgate.app.features.freemode.presentation.play.viewmodel.FreemodeEvent
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.features.freemode.presentation.play.viewmodel.FreemodeViewModel
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.components.SnackbarMessageType
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextField
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.MathGateTheme
import com.mathgate.app.ui.theme.PrimaryButton
import com.mathgate.app.ui.preview.PreviewData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreemodeScreen(
    onBackNavigate: () -> Unit,
    viewModel: FreemodeViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                is FreemodeEffect.SuccessSnackbar -> {
                    snackbarHostState.showSnackbar(AppSnackbarVisuals(
                        message = it.message,
                        type = SnackbarMessageType.SUCCESS
                    ))
                }
                is FreemodeEffect.ErrorSnackbar -> {
                    snackbarHostState.showSnackbar(AppSnackbarVisuals(
                        message = it.message,
                        type = SnackbarMessageType.ERROR
                    ))
                }
                is FreemodeEffect.NavigateBack -> {onBackNavigate()}
            }
        }
    }

    when {
        state.question == null -> {
            LoadingScreen()
        }
        user == null -> {
            EmptyState("Не удалось получить необходимую информацию")
        }
        else -> {
            AppScaffold(
                snackbarHost = {
                    AppSnackbarHost(snackbarHostState)
                },
                modifier = Modifier.fillMaxSize(),
                hasBackButton = true,
                onBackClick = { viewModel.onEvent(FreemodeEvent.OnBackClick) }
            ) {
                FreemodeContent(
                    user = user!!,
                    question = state.question!!,
                    input = state.answerInput,
                    onInput = {new -> viewModel.onEvent(FreemodeEvent.OnAnswerInput(new))},
                    onAnswer = {viewModel.onEvent(FreemodeEvent.OnAnswer)},
                    isError = state.isError,
                    difficulty = viewModel.difficulty
                )
            }
        }
    }

}

@Composable
private fun FreemodeContent(
    user: User,
    question: FreemodeQuestion,
    input: String,
    onInput: (new: String) -> Unit,
    onAnswer: () -> Unit,
    isError: Boolean,
    difficulty: FreemodeDifficulty
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

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
                            imageVector = when (difficulty) {
                                FreemodeDifficulty.EASY -> Icons.Default.SignalCellularAlt1Bar
                                FreemodeDifficulty.MEDIUM -> Icons.Default.SignalCellularAlt2Bar
                                FreemodeDifficulty.HARD -> Icons.Default.SignalCellularAlt
                            },
                            contentDescription = null
                        )
                        Text(
                            text = difficulty.label,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    question.blocks.forEach { block ->
                        when (block.type) {
                            FreemodeBlockType.TEXT -> Text(
                                text = block.content,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            FreemodeBlockType.LATEX -> Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Latex(
                                    latex = block.content,
                                    config = LatexConfig(
                                        fontSize = 24.sp,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ваш ответ",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(4.dp))

        AppTextField(
            value = input,
            onValueChange = { newValue: String ->
                onInput(newValue)
            },
            label = "Введите свой ответ",
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            onClick = onAnswer,
            modifier = Modifier.fillMaxWidth(),
            text = "Ответить",
        )
    }
}


@Composable @Preview(name = "Свободный режим · задача", showBackground = true, showSystemUi = true)
private fun FreemodePlayPreview() {
    MathGateTheme {
        AppScaffold(hasBackButton = true, modifier = Modifier.fillMaxSize()) {
            FreemodeContent(
                user = PreviewData.user,
                input = "-2",
                question = PreviewData.freemodeQuadratic,
                isError = false,
                onAnswer = {},
                onInput = {},
                difficulty = FreemodeDifficulty.HARD
            )
        }
    }
}

@Composable
@Preview(
    name = "Свободный режим · тригонометрия (тёмная)",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun FreemodePlayDarkPreview() {
    MathGateTheme(darkTheme = true) {
        AppScaffold(hasBackButton = true, modifier = Modifier.fillMaxSize()) {
            FreemodeContent(
                user = PreviewData.user,
                input = "0,5",
                question = PreviewData.freemodeTrig,
                isError = false,
                onAnswer = {},
                onInput = {},
                difficulty = FreemodeDifficulty.MEDIUM
            )
        }
    }
}