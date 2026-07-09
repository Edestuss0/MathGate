package com.mathgate.app.features.freemode.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import com.mathgate.app.domain.freemode.entity.FreemodeBlockType
import com.mathgate.app.features.freemode.viewmodel.FreemodeViewModel
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.components.FormulaPayloadItem
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextField
import com.mathgate.app.ui.theme.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreemodeScreen(
    onBackClick: () -> Unit,
    viewModel: FreemodeViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    when {
        state.question == null -> EmptyState("Урок не получен")
        else -> {
            AppScaffold(
                snackbarHost = {
                    AppSnackbarHost(snackbarHostState)
                },
                modifier = Modifier.fillMaxSize(),
                hasBackButton = true,
                onBackClick = onBackClick
            ) { innerPadding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Серия: ${state.streak}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                state.question!!.blocks.forEach { block ->
                                    when (block.type) {
                                        FreemodeBlockType.TEXT -> Text(
                                            text = block.content,
                                            style = MaterialTheme.typography.bodyLarge
                                        )

                                        FreemodeBlockType.LATEX -> Box(
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Latex(
                                                latex = block.content,
                                                config = LatexConfig(
                                                    fontSize = 24.sp,
                                                ),
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            AppTextField(
                                value = state.answerInput,
                                onValueChange = { newValue: String ->
                                    viewModel.onInputChange(newValue)
                                },
                                label = "Введите свой ответ",
                                isError = state.isError,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.onAnswer()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        Text(text = "Ответить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

}