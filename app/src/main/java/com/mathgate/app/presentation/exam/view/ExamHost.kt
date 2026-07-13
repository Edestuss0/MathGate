package com.mathgate.app.presentation.exam.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.presentation.exam.viewmodel.ExamViewModel
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
                            viewModel.loadType(type)
                            navController.navigate("play")
                        },
                        onThemesClick = {type ->
                            viewModel.loadType(type)
                            viewModel.loadThemes()
                            navController.navigate("themes")
                        },
                        user = user!!
                    )
                }
            }
        }
        composable("themes") {
            when {
                state.isLoading -> {
                    LoadingScreen()
                }
                state.themes == null -> {
                    EmptyState("Не удалось получить темы")
                }
                else -> {
                    ExamThemesScreen(
                        themes = state.themes!!,
                        onStartClick = { number ->
                            viewModel.getNewQuestion(number)
                            navController.navigate("play/$number")
                        },
                        onBackClick = {navController.popBackStack()}
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
                        user = user!!,
                        number = null
                    )
                }
            }
        }

        composable(
            route = "play/{number}",
            arguments = listOf(
                navArgument("number") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val number = backStackEntry.arguments?.getInt("number")

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
                        user = user!!,
                        number = number
                    )
                }
            }
        }
    }
}