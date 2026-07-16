package com.mathgate.app.features.exam.presentation.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.features.exam.presentation.home.viewmodel.ExamHomeEffects
import com.mathgate.app.features.exam.presentation.home.viewmodel.ExamHomeEvent
import com.mathgate.app.features.exam.presentation.home.viewmodel.ExamHomeViewModel
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextButton
import com.mathgate.app.ui.theme.EmptyState
import com.mathgate.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamHomeScreen(
    viewModel: ExamHomeViewModel = hiltViewModel(),
    onStartNavigate: (type: String) -> Unit,
    onThemesNavigate: (type: String) -> Unit,
) {
    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ExamHomeEffects.OnThemesNavigate -> {onThemesNavigate(effect.type)}
                is ExamHomeEffects.OnPlayNavigate -> {onStartNavigate(effect.type)}
            }
        }
    }

    AppScaffold(Modifier.fillMaxSize()) {
        when {
            user == null -> {
                EmptyState("Не удалось получить необходимую информацию")
            }
            else -> {
                ExamHomeContent(
                    onStartClick = { viewModel.onEvent(ExamHomeEvent.OnPlayClick) },
                    user = user!!,
                    onThemesClick = { viewModel.onEvent(ExamHomeEvent.OnThemesClick) }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExamHomeContent(
    onStartClick: (type: ExamTypes) -> Unit,
    onThemesClick: (type: ExamTypes) -> Unit,
    user: User
) {
    var isDropdownOpen by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<ExamTypes>(ExamTypes.EGE) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Задачи из экзаменов",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

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
                                text = "Верных: ${user.examSuccessRate}%",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            AppCard() {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = "Решайте задачи из экзаменов в формате свободного режима",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = isDropdownOpen,
                        onExpandedChange = { isDropdownOpen = it }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                            shape = MaterialTheme.shapes.medium,
                            value = selectedType.label,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Тип экзамена") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownOpen) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = isDropdownOpen,
                            onDismissRequest = { isDropdownOpen = false }
                        ) {
                            ExamTypes.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.label) },
                                    onClick = {
                                        selectedType = option
                                        isDropdownOpen = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                }
            }
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                onClick = {onStartClick(selectedType)},
                text = "Начать"
            )
            Spacer(Modifier.height(16.dp))
            AppTextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {onThemesClick(selectedType)},
                text = "К списку заданий"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExamHomeContentPreview() {
    ExamHomeContent(
        onStartClick = {},
        user = User("Traktoristka", 12, 1000, 54, 24, 24, listOf(true, true, false, true), listOf(true, true, false, true), true),
        onThemesClick = {}
    )
}