package com.mathgate.app.features.user.presentation.profile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.features.user.presentation.profile.viewmodel.ProfileViewModel
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.AppTextButton
import com.mathgate.app.ui.theme.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isDeleteAccountModalOpen by remember { mutableStateOf<Boolean>(false) }
    val user by viewModel.user.collectAsState()

    when {
        state.isLoading -> {
            LoadingScreen()
        }
        user == null -> {
            EmptyState("Не удалось получить информацию о пользователе")
        }
        else -> {
            ProfileContent(user = user!!, onDeleteAccount = { isDeleteAccountModalOpen = true })
        }
    }


    if (isDeleteAccountModalOpen) {
        AlertDialog(
            onDismissRequest = {
                isDeleteAccountModalOpen = false
            },
            title = { Text("Подтверждение") },
            text = { Text("Вы уверены что хотите удалить аккаунт?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAccount()
                        isDeleteAccountModalOpen = false
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                Button(
                    onClick = { isDeleteAccountModalOpen = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun ProfileContent(
    user: User,
    onDeleteAccount: () -> Unit
) {
    AppScaffold(modifier = Modifier.fillMaxSize())  { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))
            Icon(
                imageVector = Icons.Default.Person,
                modifier = Modifier.size(64.dp),
                contentDescription = null
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    ProfileContentRow(
                        title = "Уровень:",
                        icon = Icons.Default.MilitaryTech,
                        content = user.level.toString()
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileContentRow(
                        title = "Опыт:",
                        icon = Icons.Default.Star,
                        content = user.experience.toString()
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileContentRow(
                        title = "Лучшая серия ответов:",
                        icon = Icons.Default.Bolt,
                        content = user.bestStreak.toString()
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                   Text(
                       text = "Свободный режим",
                       style = MaterialTheme.typography.titleMedium
                   )
                   Spacer(Modifier.height(8.dp))
                   ProfileContentRow(
                        title = "Верных ответов:",
                        icon = Icons.Default.Check,
                        content = "${user.freemodeSuccessRate}%"
                   )
                   Spacer(Modifier.height(16.dp))
                    AppTextButton(
                        text = "Подробнее",
                        onClick = {}
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Экзамены",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    ProfileContentRow(
                        title = "Верных ответов:",
                        icon = Icons.Default.Check,
                        content = "${user.examSuccessRate}%"
                    )
                    Spacer(Modifier.height(16.dp))
                    AppTextButton(
                        text = "Подробнее",
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContentRow(
    icon: ImageVector,
    title: String,
    content: String
) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = ""
            )
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileContentPreview() {
    ProfileContent(
        user = User("Traktoristka", 12, 650, 24, 24, listOf(true, true, false, true), emptyList(), true),
        onDeleteAccount = {}
    )
}
