package com.mathgate.app.features.campaign

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignScreen(
    viewModel: CampaignViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.initializeData()
    }

    val state by viewModel.state.collectAsState()
    val currentCampaign by viewModel.currentCampaign.collectAsState()
    var answerInput by remember { mutableStateOf<String>("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )

            viewModel.onMessageShown()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val backgroundColor = if (state.isError) {
                    Color(0xFFE53935)
                } else {
                    Color(0xFF43A047)
                }

                Snackbar(
                    snackbarData = data,
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    actionColor = Color.Yellow,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Кампания", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
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
            modifier = Modifier.fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(currentCampaign?.question ?: "Не удалось найти вопрос", fontSize = 16.sp, fontWeight = FontWeight.Bold,)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        label = { Text("Введите свой ответ") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        isError = state.isError,
                        value = answerInput,
                        onValueChange = { newValue: String ->
                            val isValidInteger = newValue.isEmpty() ||
                                    newValue == "-" ||
                                    newValue.toIntOrNull() != null

                            if (isValidInteger) {
                                answerInput = newValue
                            }
                        },
                    )
                }
            }
            Button(
                onClick = {
                    viewModel.onAnswer(if (!(answerInput == "")) answerInput.toInt() else return@Button)
                    answerInput = ""
                          },
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Ответить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}