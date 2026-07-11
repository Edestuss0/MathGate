package com.mathgate.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mathgate.app.features.education.GradesScreen
import com.mathgate.app.features.education.ThemeScreen
import com.mathgate.app.features.exam.view.ExamHost
import com.mathgate.app.features.freemode.view.FreemodeHomeScreen
import com.mathgate.app.features.freemode.view.FreemodeHost
import com.mathgate.app.features.profile.ProfileScreen
import com.mathgate.app.ui.theme.AppScaffold

enum class Screens(val route: String, val title: String, val icon: ImageVector) {
    FreemodeHome("freemode_home", "Свободный", Icons.Default.Keyboard),
    GradesScreen("education_home", "Обучение", Icons.Default.MenuBook),
    OgeHome("oge_home", "Экзамены", Icons.Default.School),
    Profile("profile", "Профиль", Icons.Default.Person)
}

@Composable
fun MainPage(
    rootNavController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentGrade = 0

    AppScaffold(
        bottomBar = {
            NavigationBar() {
                Screens.entries.forEach {screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = {Text(screen.title)},
                        onClick = {
                            tabNavController.navigate(
                                screen.route
                            ) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(
                                    tabNavController
                                        .graph
                                        .startDestinationId
                                ) {
                                    saveState = true
                                }
                            }
                        },
                    )
                }
            }
        }
    ) { contentPadding ->

        NavHost(
            modifier = Modifier.fillMaxSize().padding(bottom = contentPadding.calculateBottomPadding(), top = contentPadding.calculateTopPadding()),
            navController = tabNavController,
            startDestination = Screens.GradesScreen.route
        ) {

            composable(Screens.GradesScreen.route) {
                if (currentGrade == null) {
                    GradesScreen()
                } else {
                    ThemeScreen(
                        grade = currentGrade!!,
                        onLessonClick = {id -> rootNavController.navigate("lesson/${id}")}
                    )
                }
            }

            composable(Screens.FreemodeHome.route) {
                FreemodeHost()
            }

            composable(Screens.OgeHome.route) {
                ExamHost()
            }


            composable(Screens.Profile.route) {
                ProfileScreen()
            }
        }
    }
}