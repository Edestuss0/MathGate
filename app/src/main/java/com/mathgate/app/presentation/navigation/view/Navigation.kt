package com.mathgate.app.presentation.navigation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathgate.app.presentation.navigation.viewmodel.AuthState
import com.mathgate.app.presentation.navigation.viewmodel.MainViewModel
import com.mathgate.app.presentation.start_page.StartPage
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

    }
}