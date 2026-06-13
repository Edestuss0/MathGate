package com.mathgate.app.features.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.core.entities.ChoiceQuestionLessonPayload
import com.mathgate.app.core.entities.FormulaLessonPayload
import com.mathgate.app.core.entities.InputQuestionLessonPayload
import com.mathgate.app.core.entities.LessonBlockType
import com.mathgate.app.core.entities.SvgLessonPayload
import com.mathgate.app.core.entities.TextLessonPayload
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.components.ChoiceQuestionPayloadItem
import com.mathgate.app.ui.components.FormulaPayloadItem
import com.mathgate.app.ui.components.InputQuestionPayloadItem
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.components.SvgImageView
import com.mathgate.app.ui.components.TextPayloadItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    viewModel: LessonViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading || state.lesson == null) {
        LoadingScreen()
        return
    }

    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(key1 = state.snackbarMessage) {
        state.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.lesson?.title ?: "Нет данных", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (state.currentPage == null) {
                Box(
                  modifier = Modifier.fillMaxWidth(),
                  contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Урок пуст",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                return@Scaffold
            }

            state.currentPage!!.blocks.forEach { block ->
                when (block.blockType) {
                    LessonBlockType.TEXT -> TextPayloadItem(payload = block.payload as TextLessonPayload)
                    LessonBlockType.FORMULA -> FormulaPayloadItem(payload = block.payload as FormulaLessonPayload)
                    LessonBlockType.INPUT_QUESTION -> InputQuestionPayloadItem(
                        payload = block.payload as InputQuestionLessonPayload,
                        isError = state.isError,
                        onRightAnswer = {
                            viewModel.onAnswer(true)
                            if (state.isLastPage) {
                                onBackClick()
                            }
                                        },
                        onWrongAnswer = {viewModel.onAnswer(false)},
                        isLocked = state.isPageLocked,
                    )
                    LessonBlockType.CHOICE_QUESTION -> ChoiceQuestionPayloadItem(
                        payload = block.payload as ChoiceQuestionLessonPayload,
                        onRightAnswer = {
                            viewModel.onAnswer(true)
                            if (state.isLastPage) {
                                onBackClick()
                            }
                                        },
                        onWrongAnswer = {viewModel.onAnswer(false)},
                        isLocked = state.isPageLocked,
                    )

                    LessonBlockType.SVG -> {
                        val payload = block.payload as SvgLessonPayload
                        SvgImageView(url = payload.svg)
                    }
                    else -> TextPayloadItem(payload = TextLessonPayload(""))
                }
            }

            Spacer(Modifier.weight(1f))

            if (!state.isPageLocked){
                Button(
                    onClick = {
                        if (state.isLastPage) {
                            onBackClick()
                        }
                        viewModel.nextPage()
                              },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(
                        text = if (!state.isLastPage) "Дальше" else "Завершить",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}