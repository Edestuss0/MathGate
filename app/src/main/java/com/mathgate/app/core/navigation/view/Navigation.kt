package com.mathgate.app.core.navigation.view

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.core.ad.AdManager
import com.mathgate.app.core.app.findActivity
import com.mathgate.app.core.navigation.utils.navigateWithAd
import com.mathgate.app.core.navigation.viewmodel.AuthState
import com.mathgate.app.core.navigation.viewmodel.MainViewModel
import com.mathgate.app.features.exam.presentation.play.view.ExamPlayScreen
import com.mathgate.app.features.exam.presentation.themes.view.ExamThemesScreen
import com.mathgate.app.features.freemode.presentation.play.view.FreemodeScreen
import com.mathgate.app.features.trigonometry.presentation.view.TrigonometryScreen
import com.mathgate.app.features.user.presentation.exam_stats.view.ExamStatsScreen
import com.mathgate.app.features.user.presentation.freemode_stats.view.FreemodeStatsScreen
import com.mathgate.app.features.user.presentation.start_page.view.StartPage
import com.mathgate.app.ui.components.LoadingScreen

sealed class Screen(val route: String) {
    data object StartPage : Screen("start_page")
    data object MainPage : Screen("main")
    data object ExamPlay : Screen("exam/play/{type}") {
        fun navigate(type: String) = "exam/play/$type"
    }
    data object ExamPlayTheme : Screen("exam/play/{type}/{number}") {
        fun navigate(type: String, number: Int) = "exam/play/$type/$number"
    }
    data object ExamThemes : Screen("exam/themes/{type}") {
        fun navigate(type: String) = "exam/themes/$type"
    }
    data object FreemodePlay : Screen("freemode/play/{difficulty}") {
        fun navigate(difficulty: String) = "freemode/play/$difficulty"
    }
    data object ExamStats : Screen("exam/stats")
    data object FreemodeStats : Screen("freemode/stats")
    data object Trigonometry : Screen("trigonometry")
}

@Composable
fun AppNavigation(adManager: AdManager) {

    val rootNavController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()

    val activity = LocalContext.current.findActivity()

    if (authState is AuthState.Loading) {
        LoadingScreen()
    }

    val startDestination = when (authState) {
        is AuthState.Registered -> Screen.MainPage.route
        else -> Screen.StartPage.route
    }

    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {

        composable(Screen.StartPage.route) {
            StartPage()
        }

        composable(Screen.MainPage.route) {
            MainPage(rootNavController, adManager)
        }

        composable(
            route = Screen.ExamPlay.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType }
            )
        ) {
            ExamPlayScreen(
                onBackNavigate = { rootNavController.popBackStack() },
            )
        }

        composable(
            route = Screen.ExamPlayTheme.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("number") { type = NavType.IntType }
            )
        ) {
            ExamPlayScreen(
                onBackNavigate = { rootNavController.popBackStack() },
            )
        }

        composable(
            route = Screen.ExamThemes.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType }
            )
        ) {
            ExamThemesScreen(
                onBackNavigate = { rootNavController.popBackStack() },
                onPlayNavigate = { type, number ->
                    rootNavController.navigateWithAd(
                        route = Screen.ExamPlayTheme.navigate(type, number),
                        activity = activity,
                        adManager = adManager,
                    )
                }
            )
        }

        composable(
            route = Screen.FreemodePlay.route,
            arguments = listOf(
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) {
            FreemodeScreen(
                onBackNavigate = { rootNavController.popBackStack() }
            )
        }

        composable(Screen.Trigonometry.route) {
            TrigonometryScreen { rootNavController.popBackStack() }
        }

        composable(Screen.ExamStats.route) {
            ExamStatsScreen(
                onBackNavigate = { rootNavController.popBackStack() }
            )
        }

        composable(Screen.FreemodeStats.route) {
            FreemodeStatsScreen(
                onBackNavigate = { rootNavController.popBackStack() }
            )
        }
    }
}