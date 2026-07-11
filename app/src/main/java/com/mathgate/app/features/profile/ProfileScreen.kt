package com.mathgate.app.features.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mathgate.app.domain.user.entity.User
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isDeleteAccountModalOpen by remember { mutableStateOf<Boolean>(false) }

    when {
        state.isLoading -> {
            LoadingScreen()
        }
        state.user == null -> {
            EmptyState("Не удалось получить информацию о пользователе")
        }
        else -> {
            ProfileContent(user = state.user!!, onDeleteAccount = { isDeleteAccountModalOpen = true })
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Default.Person,
                            contentDescription = ""
                        )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = user.username,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Уровень: ${user.level}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            ProfileContentRow(
                icon = Icons.Default.Star,
                content = "Опыт: ${user.experience}"
            )

            ProfileContentRow(
                icon = Icons.Default.LineAxis,
                content = "Лучшая серия: ${user.bestStreak}"
            )

            Button(
                onClick = onDeleteAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = "Удалить аккаунт", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileContentRow(
    icon: ImageVector,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = icon,
                    contentDescription = ""
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = content,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileContentPreview() {
    ProfileContent(
        user = User(),
        onDeleteAccount = {}
    )
}
