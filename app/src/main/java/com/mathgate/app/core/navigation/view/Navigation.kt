package com.mathgate.app.core.navigation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.core.navigation.viewmodel.AuthState
import com.mathgate.app.core.navigation.viewmodel.MainViewModel
import com.mathgate.app.features.exam.presentation.play.view.ExamPlayScreen
import com.mathgate.app.features.exam.presentation.themes.view.ExamThemesScreen
import com.mathgate.app.features.freemode.presentation.play.view.FreemodeScreen
import com.mathgate.app.features.user.presentation.start_page.view.StartPage
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
            route = "exam/play/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) {
            ExamPlayScreen(
                onBackNavigate = {rootNavController.popBackStack()},
            )
        }

        composable(
            route = "exam/play/{type}/{number}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
                navArgument("number") {
                    type = NavType.IntType
                }
            )
        ) {
            ExamPlayScreen(
                onBackNavigate = {rootNavController.popBackStack()},
            )
        }

        composable(
            route = "exam/themes/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) {
            ExamThemesScreen(
                onBackNavigate = {rootNavController.popBackStack()},
                onPlayNavigate = {type, number -> rootNavController.navigate("exam/play/$type/$number")}
            )
        }

        composable(
            route = "freemode/play/{difficulty}",
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                }
            )
        ) {
            FreemodeScreen(
                onBackNavigate = {rootNavController.popBackStack()}
            )
        }

    }
}