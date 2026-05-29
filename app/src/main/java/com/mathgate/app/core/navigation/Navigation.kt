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
import com.mathgate.app.features.campaign.CampaignScreen
import com.mathgate.app.features.freemode.FreemodeScreen
import com.mathgate.app.features.start_page.StartPage

@Composable
fun AppNavigation() {

    val rootNavController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val isRegistered by viewModel.isRegistered.collectAsState()

    NavHost(
        navController = rootNavController,
        startDestination = if (isRegistered) "main" else "start_page"
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

            val difficulty =
                backStackEntry.arguments?.getString("difficulty") ?: "easy"

            FreemodeScreen(
                difficulty = difficulty,
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable("campaign_play") {
            CampaignScreen(onBackClick = {rootNavController.popBackStack()})
        }
    }
}