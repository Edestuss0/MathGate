package com.mathgate.app.features.user.presentation.start_page.view

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.features.user.presentation.start_page.viewmodel.StartPageEffects
import com.mathgate.app.features.user.presentation.start_page.viewmodel.StartPageEvent
import com.mathgate.app.features.user.presentation.start_page.viewmodel.StartPageViewModel
import com.mathgate.app.ui.components.AppSnackbarHost
import com.mathgate.app.ui.components.AppSnackbarVisuals
import com.mathgate.app.ui.components.SnackbarMessageType
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextField
import com.mathgate.app.ui.theme.MathGateTheme
import com.mathgate.app.ui.theme.PrimaryButton
import androidx.core.net.toUri

@Composable
fun StartPage(
    viewModel: StartPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is StartPageEffects.SuccessSnackbar -> {
                    snackbarHostState.showSnackbar(AppSnackbarVisuals(
                        message = effect.message,
                        type = SnackbarMessageType.SUCCESS
                    ))
                }
                is StartPageEffects.ErrorSnackbar -> {
                    snackbarHostState.showSnackbar(AppSnackbarVisuals(
                        message = effect.message,
                        type = SnackbarMessageType.ERROR
                    ))
                }
            }
        }
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        StartPageContent(
            nameInput = state.nameInput,
            isError = state.isError,
            onNameChange = { new -> viewModel.onEvent(StartPageEvent.OnInputName(new)) },
            onRegister = { viewModel.onEvent(StartPageEvent.Register) },
            onPolicyClick = {
                val intent = Intent(Intent.ACTION_VIEW, "https://help-studio.ru/mathgate/policy".toUri())
                context.startActivity(intent)
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun StartPageContent(
    nameInput: String,
    isError: Boolean,
    onNameChange: (String) -> Unit,
    onRegister: () -> Unit,
    onPolicyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Введите своё имя",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Так вы сможете сохранить свой прогресс",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                AppTextField(
                    value = nameInput,
                    onValueChange = onNameChange,
                    label = "Введите своё имя",
                    isError = isError
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = "Зарегистрироваться",
                    onClick = onRegister
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Нажимая кнопку \"Зарегистрироваться\", вы соглашаетесь с действующей политикой конфиденциальности приложения",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable { onPolicyClick() }
        )
    }
}

@Preview(name = "Регистрация · Приветствие", showBackground = true, showSystemUi = true)
@Composable
private fun StartPageContentPreview() {
    MathGateTheme {
        AppScaffold(modifier = Modifier.fillMaxSize()) { padding ->
            StartPageContent(
                nameInput = "Алексей",
                isError = false,
                onNameChange = {},
                onRegister = {},
                onPolicyClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(
    name = "Регистрация · Приветствие (тёмная)",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StartPageContentDarkPreview() {
    MathGateTheme(darkTheme = true) {
        AppScaffold(modifier = Modifier.fillMaxSize()) { padding ->
            StartPageContent(
                nameInput = "",
                isError = false,
                onNameChange = {},
                onRegister = {},
                onPolicyClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}