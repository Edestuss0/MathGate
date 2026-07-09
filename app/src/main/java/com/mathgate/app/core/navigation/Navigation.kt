package com.mathgate.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.features.freemode.view.FreemodeScreen
import com.mathgate.app.features.lesson.LessonScreen
import com.mathgate.app.features.start_page.StartPage
import com.mathgate.app.ui.components.LoadingScreen

@Composable
fun AppNavigation() {

    val rootNavController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()

    if (authState is AuthState.Loading) {
        LoadingScreen()
    }

    val startDestination = when (authState) {
        is AuthState.Registered -> "main"
        else -> "start_page"
    }

    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {

        composable("start_page") {
            StartPage()
        }

        composable("main") {
            MainPage(rootNavController)
        }

        composable(
            route = "play/{difficulty}",
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                }
            )
        ) {
            FreemodeScreen(
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable(
            route = "lesson/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            LessonScreen(
                onBackClick = {rootNavController.popBackStack()},
            )
        }

    }
}