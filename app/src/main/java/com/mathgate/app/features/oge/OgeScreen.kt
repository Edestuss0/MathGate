package com.mathgate.app.features.oge

import LoadingScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.core.entities.FormulaBlock
import com.mathgate.app.core.entities.ImageBlock
import com.mathgate.app.core.entities.TextBlock
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.components.SvgImageView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OgeScreen(
    onBackClick: () -> Unit,
    viewModel: OgeViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()
    var answerInput by remember { mutableStateOf("") }

    LaunchedEffect(key1 = state.snackbarMessage) {
        state.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    if (state.isLoading || (state.question == null)) {
        LoadingScreen()
        return
    }

    Scaffold(
        snackbarHost = {
            AppSnackbarHost(snackbarHostState)
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Свободный режим", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            state.question?.blocks?.forEach { block ->
                when (block) {
                    is TextBlock -> {
                        Text(text = block.text, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp))
                    }

                    is ImageBlock -> {
                        SvgImageView(block.url)
                    }

                    is FormulaBlock -> {
                        SvgImageView(
                            url = block.url,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .heightIn(max = 60.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                onValueChange = {newValue ->
                    answerInput = newValue
                },
                value = answerInput,
                isError = state.isError,
                label = { Text("Введите свой ответ") },
            )

            Button(
                onClick = {
                    viewModel.onAnswer(answerInput)
                    answerInput = ""
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Ответить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = {
                        viewModel.getNewQuestion()
                        answerInput = ""
                    }
                ) {
                    Text(text = "Пропустить задачу", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}