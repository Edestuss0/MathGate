package com.mathgate.app.features.start_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StartPage(
    viewModel: StartPageViewModel = hiltViewModel()
) {

    var nameInput by remember { mutableStateOf<String>("") }
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(
                message = message
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
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text("Введите ваше имя")

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                label = { Text("Имя") },
                value = nameInput,
                isError = state.isError,
                onValueChange = {newValue: String ->
                    nameInput = newValue
                },
            )

            Button(
                onClick = {viewModel.register(nameInput)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Зарегистрироваться", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}