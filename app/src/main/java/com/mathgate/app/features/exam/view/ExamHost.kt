package com.mathgate.app.features.exam.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mathgate.app.features.exam.viewmodel.ExamViewModel
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.theme.EmptyState

@Composable
fun ExamHost(
    viewModel: ExamViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            when {
                state.isLoading -> {
                    LoadingScreen()
                }
                user == null -> {
                    EmptyState("Не удалось получить необходимую информацию")
                }
                else -> {
                    ExamHomeScreen(
                        onStartClick = {type ->
                            viewModel.load(type)
                            navController.navigate("play")
                        },
                        user = user!!
                    )
                }
            }
        }
        composable("play") {
            when {
                state.isLoading -> {
                    LoadingScreen()
                }
                user == null -> {
                    EmptyState("Не удалось получить необходимую информацию")
                }
                else -> {
                    ExamScreen(
                        viewModel = viewModel,
                        state = state,
                        onBackClick = {navController.popBackStack()},
                        user = user!!
                    )
                }
            }
        }
    }
}