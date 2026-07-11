package com.mathgate.app.features.exam.view

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mathgate.app.domain.exam.entity.ExamBlock
import com.mathgate.app.domain.exam.entity.ExamBlockType
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTypes
import com.mathgate.app.domain.freemode.entity.FreemodeDifficulty
import com.mathgate.app.domain.user.entity.User
import com.mathgate.app.features.exam.viewmodel.ExamState
import com.mathgate.app.features.exam.viewmodel.ExamViewModel
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.components.SvgImageView
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextButton
import com.mathgate.app.ui.theme.AppTextField
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    onBackClick: () -> Unit,
    viewModel: ExamViewModel,
    state: ExamState,
    user: User
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    AppScaffold(
        hasBackButton = true,
        onBackClick = onBackClick,
        topBarText = "Экзамены"
    ) {
        when {
            state.isLoading && state.question == null -> {
                LoadingScreen()
            }
            state.question == null -> {
                EmptyState("Не удалось найти вопрос")
            }
            else -> {
                ExamContent(
                    isError = state.isError,
                    question = state.question,
                    isAnswered = state.isAnswered,
                    onAnswer = {input -> viewModel.onAnswer(input)},
                    skipQuestion = {viewModel.skipQuestion()},
                    getNewQuestion = {viewModel.getNewQuestion()},
                    type = state.type ?: ExamTypes.EGE,
                    user = user
                )
            }
        }
    }
}

@Composable
private fun ExamContent(
    isAnswered: Boolean,
    question: ExamQuestion,
    isError: Boolean,
    onAnswer: (input: String) -> Unit,
    skipQuestion: () -> Unit,
    getNewQuestion: () -> Unit,
    user: User,
    type: ExamTypes
) {
    var answerInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null
                            )
                            Text(
                                text = type.label,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            AppCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!isAnswered) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        question.blocks.forEach { block ->
                            when (block.type) {
                                ExamBlockType.TEXT -> {
                                    Text(
                                        text = block.content,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                ExamBlockType.FORMULA -> {
                                    SvgImageView(
                                        url = block.content,
                                        modifier = Modifier.height(32.dp)
                                    )
                                }

                                ExamBlockType.IMAGE -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        SvgImageView(
                                            url = block.content,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        question.solutionBlocks.forEach { block ->
                            when (block.type) {
                                ExamBlockType.TEXT -> {
                                    Text(
                                        text = block.content,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                ExamBlockType.FORMULA -> {
                                    SvgImageView(
                                        url = block.content,
                                        modifier = Modifier.height(32.dp)
                                    )
                                }

                                ExamBlockType.IMAGE -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        SvgImageView(
                                            url = block.content,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (!isAnswered) {
                Text(
                    text = "Ваш ответ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(4.dp))

                AppTextField(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { newValue -> answerInput = newValue },
                    value = answerInput,
                    isError = isError,
                    label = "Введите свой ответ",
                )

                Spacer(Modifier.height(16.dp))

                PrimaryButton(
                    onClick = {
                        onAnswer(answerInput)
                        answerInput = ""
                    },
                    text = "Ответить"
                )

                Spacer(Modifier.height(8.dp))

                AppTextButton(
                    text = "Пропустить задачу",
                    onClick = {
                        skipQuestion()
                        answerInput = ""
                    }
                )
            } else {
                PrimaryButton(
                    onClick = { getNewQuestion() },
                    text = "Следующий вопрос"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    ExamContent(
        question = ExamQuestion(
            answer = "0.5",
            blocks = listOf(
                ExamBlock(type = ExamBlockType.TEXT, "Найдите значение выражения"),
                ExamBlock(type = ExamBlockType.FORMULA, "sin \\frac{\\pi}{6}")
            ),
            solutionBlocks = listOf(
                ExamBlock(type = ExamBlockType.TEXT, "Найдите значение выражения"),
                ExamBlock(type = ExamBlockType.FORMULA, "sin \\frac{\\pi}{6}")
            ),
            id = 1314
        ),
        skipQuestion = {},
        getNewQuestion = {},
        isAnswered = false,
        onAnswer = {},
        isError = false,
        user = User("Traktoristka", 12, 54, 24, 24, listOf(true, true, false, true), emptyList(), true),
        type = ExamTypes.OGE
    )
}