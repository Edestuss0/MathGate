package com.mathgate.app.features.education

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.core.entities.EducationTheme
import com.mathgate.app.core.entities.LessonByTheme
import com.mathgate.app.ui.components.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeScreen(
    grade: Int,
    viewModel: ThemeViewModel = hiltViewModel(),
    onLessonClick: (id: Int) -> Unit
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(grade) {
        viewModel.getInitialData(grade)
    }

    if (state.isLoading || state.currentTheme == null) {
        LoadingScreen()
        return
    }

    if ((state.lessonInfoData.show) && (state.lessonInfoData.lesson != null)) {
        ModalBottomSheet(
            onDismissRequest = {viewModel.hideLessonInfo()}
        ) {
            Text(
                text = state.lessonInfoData.lesson!!.title,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = state.lessonInfoData.lesson!!.description,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
            )
            Button(
                onClick = {
                    viewModel.hideLessonInfo()
                    onLessonClick(state.lessonInfoData.lesson!!.id)
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Начать урок", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (state.lessons.size > 0) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 90.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(items = state.lessons) { index, lesson ->
                        LessonTrackItem(
                            lesson = lesson,
                            index = index,
                            onClick = {
                                viewModel.showLessonInfo(lesson)
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Уроки не найдены", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            ExpandableThemeCard(
                themes = state.themes,
                expanded = state.themesExpanded,
                onExpand = {viewModel.showThemes()},
                onCollapse = {viewModel.hideThemes()},
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(10f)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    ),
                onChangeTheme = {theme -> viewModel.changeCurrentTheme(theme)},
                currentTheme = state.currentTheme,
                onGradeClick = { viewModel.onGradeClick() }
            )
        }
    }
}

@Composable
private fun LessonTrackItem(
    lesson: LessonByTheme,
    index: Int,
    onClick: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme

    val container = if (lesson.orderIndex == 0) {
        scheme.primaryContainer
    } else {
        scheme.surfaceContainerHigh
    }


    val xOffset = when (index % 18) {
        0  -> -64.dp
        1 -> -32.dp
        2 -> 8.dp
        3 -> 32.dp
        4 -> 0.dp
        5 -> -24.dp
        6 -> 8.dp
        7 -> 32.dp
        8 -> 56.dp
        9 -> 24.dp
        10 -> 48.dp
        11 -> 8.dp
        12 -> 32.dp
        13 -> 0.dp
        14 -> -24.dp
        15 -> 8.dp
        16 -> -16.dp
        else -> -48.dp
    }


    LessonNode(
        lesson = lesson,
        container = container,
        onClick = onClick,
        modifier = Modifier
            .offset(x = xOffset, y = 0.dp)
            .padding(16.dp)
    )
}

@Composable
fun LessonNode(
    lesson: LessonByTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    container: Color,
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(container)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null
        )
    }
}

@Composable
fun ExpandableThemeCard(
    currentTheme: EducationTheme?,
    themes: List<EducationTheme>,
    expanded: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier,
    onChangeTheme: (theme: EducationTheme) -> Unit,
    onGradeClick: () -> Unit
) {
    val height by animateDpAsState(
        targetValue = if (expanded) 800.dp else 80.dp,
        label = ""
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable {
                if (!expanded) onExpand() else onCollapse()
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = currentTheme?.name ?: "Нет данных",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (currentTheme != null) "Тема ${currentTheme!!.grade} класса" else "Нет данных",
                fontSize = 16.sp
            )

            AnimatedVisibility(expanded) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))

                    val scheme = MaterialTheme.colorScheme

                    themes.forEach { theme ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    if (currentTheme != theme) {
                                        onChangeTheme(theme)
                                    }
                                    onCollapse()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = scheme.primaryContainer,
                                contentColor = scheme.onPrimaryContainer
                            ),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = theme.name,
                                    modifier = Modifier.padding(12.dp),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    TextButton(
                        onClick = onGradeClick
                    ) {
                        Text("К выбору класса")
                    }

                    Button(
                        onClick = onCollapse,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        }
    }
}