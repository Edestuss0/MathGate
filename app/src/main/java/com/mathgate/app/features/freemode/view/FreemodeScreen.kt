package com.mathgate.app.features.freemode.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularAlt1Bar
import androidx.compose.material.icons.filled.SignalCellularAlt2Bar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import com.mathgate.app.domain.freemode.entity.FreemodeBlockType
import com.mathgate.app.domain.freemode.entity.FreemodeDifficulty
import com.mathgate.app.domain.freemode.entity.FreemodeQuestion
import com.mathgate.app.domain.freemode.entity.FreemodeQuestionBlock
import com.mathgate.app.domain.user.entity.User
import com.mathgate.app.features.freemode.viewmodel.FreemodeState
import com.mathgate.app.features.freemode.viewmodel.FreemodeViewModel
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.components.FormulaPayloadItem
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextField
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreemodeScreen(
    onBackClick: () -> Unit,
    viewModel: FreemodeViewModel = hiltViewModel(),
    state: FreemodeState,
    user: User
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    when {
        state.question == null -> EmptyState("Урок не получен")
        else -> {
            AppScaffold(
                snackbarHost = {
                    AppSnackbarHost(snackbarHostState)
                },
                modifier = Modifier.fillMaxSize(),
                hasBackButton = true,
                onBackClick = onBackClick
            ) {
                FreemodeContent(
                    user = user,
                    question = state.question,
                    input = state.answerInput,
                    onInput = {new -> viewModel.onInputChange(new)},
                    onAnswer = {viewModel.onAnswer()},
                    isError = state.isError,
                    difficulty = state.selectedDifficulty
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
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
                modifier = Modifier.fillMaxWidth().padding(16.dp)
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


@Composable @Preview(showBackground = true)
private fun preview() {
    val user = User("Traktoristka", 12, 4, 6, 2, listOf(true, true, false, true), emptyList(), true)
    FreemodeContent(
        user = user,
        input = "15",
        question = FreemodeQuestion(answer = "0.5", blocks = listOf(
            FreemodeQuestionBlock(type = FreemodeBlockType.TEXT, content = "Найдите значение выражения"),
            FreemodeQuestionBlock(type = FreemodeBlockType.LATEX, content = "cos \\frac{\\pi}{6}")
        )),
        isError = false,
        onAnswer = {},
        onInput = {},
        difficulty = FreemodeDifficulty.MEDIUM
    )
}