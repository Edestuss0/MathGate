package com.mathgate.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.features.campaign.CampaignScreen
import com.mathgate.app.features.lessons.LessonsScreen
import com.mathgate.app.features.freemode.FreemodeScreen
import com.mathgate.app.features.lesson_tasks.LessonTasksScreen
import com.mathgate.app.features.lessons.LessonScreen
import com.mathgate.app.features.start_page.StartPage

@Composable
fun AppNavigation() {

    val rootNavController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()

    if (authState is AuthState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize()
        )
        return
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
        ) { backStackEntry ->

            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "easy"
            FreemodeScreen(
                difficulty = difficulty,
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable(
            route = "lessons/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            LessonsScreen(
                id = id,
                onBackClick = { rootNavController.popBackStack() },
                onLessonClick = {lessonId -> rootNavController.navigate("lesson/$lessonId")},
            )
        }

        composable(
            route = "lesson/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            LessonScreen(
                lessonId = id,
                onBackClick = {rootNavController.popBackStack()},
                onStartPractice = {lessonId -> rootNavController.navigate("lesson/practice/$lessonId")}
            )
        }

        composable(
            route = "lesson/practice/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            LessonTasksScreen(
                id = id,
                onBackClick = {rootNavController.popBackStack()}
            )
        }

        composable("campaign_play") {
            CampaignScreen(
                onBackClick = {rootNavController.popBackStack()},
                onThemeClick = {id -> rootNavController.navigate("lessons/$id")}
            )
        }
    }
}