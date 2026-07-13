package com.mathgate.app.presentation.freemode.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mathgate.app.presentation.freemode.viewmodel.FreemodeViewModel
import com.mathgate.app.ui.components.LoadingScreen
import com.mathgate.app.ui.theme.EmptyState

@Composable
fun FreemodeHost(
    viewModel: FreemodeViewModel = hiltViewModel()
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
                    EmptyState("Не удалось получить информацию о пользователе")
                }
                else -> {
                    FreemodeHomeScreen(
                        onStartButtonClick = {
                            navController.navigate("play")
                            viewModel.loadData()
                        },
                        state = state,
                        viewmodel = viewModel,
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
                state.question == null -> {
                    viewModel.loadData()
                    EmptyState("Не удалось получить вопрос")
                }
                user == null -> {
                    EmptyState("Не удалось получить информацию о пользователе")
                }
                else -> {
                    FreemodeScreen(
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