package com.mathgate.app.features.freemode

import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import com.mathgate.app.core.entities.Difficulties
import com.mathgate.app.ui.theme.MathGateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreemodeHomeScreen(onStartButtonClick: (difficulty: String) -> Unit) {

    val difficulties = listOf(Difficulties.easy, Difficulties.medium, Difficulties.hard)
    var isDropdownOpen by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf(difficulties[0]) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Свободный режим",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Решайте математические примеры в свободном режиме",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = isDropdownOpen,
                onExpandedChange = { isDropdownOpen = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    value = selectedDifficulty.label,
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
                    difficulties.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                selectedDifficulty = option
                                isDropdownOpen = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onStartButtonClick(selectedDifficulty.name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Начать", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}