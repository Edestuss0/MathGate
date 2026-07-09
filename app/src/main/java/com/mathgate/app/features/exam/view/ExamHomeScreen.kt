package com.mathgate.app.features.exam.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathgate.app.domain.exam.entity.ExamTypes
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamHomeScreen(
    onStartClick: (type: ExamTypes) -> Unit
) {

    var isDropdownOpen by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<ExamTypes>(ExamTypes.EGE) }

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AppCard() {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {

                    Text(text = "Решайте задания из экзаменов", style = MaterialTheme.typography.titleLarge)

                    Spacer(Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = isDropdownOpen,
                        onExpandedChange = { isDropdownOpen = it }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable, true),
                            shape = RoundedCornerShape(16.dp),
                            value = selectedType.label,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Сложность") },
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

                    PrimaryButton(
                        onClick = {onStartClick(selectedType)},
                        text = "Начать"
                    )
                }
            }
        }
    }
}