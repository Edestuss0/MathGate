package com.mathgate.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.core.data.user.UserRepository
import com.mathgate.app.features.campaign.CampaignScreenAppRoute
import com.mathgate.app.features.freemode.FreemodeScreen
import com.mathgate.app.features.start_page.StartPage

@Composable
fun AppNavigation() {

    val rootNavController = rememberNavController()
    val context = LocalContext.current.applicationContext

    val viewModel: MainViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val repository = UserRepository(context)
                MainViewModel(repository)
            }
        }
    )

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
            CampaignScreenAppRoute(onBackClick = {rootNavController.popBackStack()})
        }
    }
}