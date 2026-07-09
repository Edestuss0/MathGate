package com.mathgate.app.features.exam.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import com.mathgate.app.domain.exam.entity.ExamBlockType
import com.mathgate.app.features.exam.viewmodel.ExamState
import com.mathgate.app.features.exam.viewmodel.ExamViewModel
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.components.SvgImageView
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
    state: ExamState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var answerInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    AppScaffold(
        hasBackButton = true,
        onBackClick = onBackClick,
        topBarText = "Экзамены"
    ) { paddingValues ->
        when {
            state.isLoading && state.question == null -> {
                LoadingScreen()
            }
            state.question == null -> {
                EmptyState("Не удалось найти вопрос")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .imePadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (!state.isAnswered) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            state.question.blocks.forEach { block ->
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

                        Spacer(Modifier.height(16.dp))

                        AppTextField(
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { newValue -> answerInput = newValue },
                            value = answerInput,
                            isError = state.isError,
                            label = "Введите свой ответ",
                        )

                        Spacer(Modifier.height(16.dp))

                        PrimaryButton(
                            onClick = {
                                viewModel.onAnswer(answerInput)
                                answerInput = ""
                            },
                            text = "Ответить"
                        )

                        Spacer(Modifier.height(8.dp))

                        AppTextButton(
                            text = "Пропустить задачу",
                            onClick = {
                                viewModel.skipQuestion()
                                answerInput = ""
                            }
                        )
                    } else {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            state.question.solutionBlocks.forEach { block ->
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

                        Spacer(Modifier.height(16.dp))

                        PrimaryButton(
                            onClick = { viewModel.getNewQuestion() },
                            text = "Следующий вопрос"
                        )
                    }
                }
            }
        }
    }
}