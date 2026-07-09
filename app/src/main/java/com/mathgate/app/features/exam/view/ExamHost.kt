package com.mathgate.app.features.exam.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mathgate.app.features.exam.viewmodel.ExamViewModel

@Composable
fun ExamHost(
    viewModel: ExamViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            ExamHomeScreen(
                onStartClick = {type ->
                    viewModel.load(type)
                    navController.navigate("play")
                }
            )
        }
        composable("play") {
            ExamScreen(
                viewModel = viewModel,
                state = state,
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}