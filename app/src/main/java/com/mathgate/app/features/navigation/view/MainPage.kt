package com.mathgate.app.features.navigation.view

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
import com.mathgate.app.features.freemode.view.FreemodeHost
import com.mathgate.app.features.navigation.viewmodel.MainViewModel
import com.mathgate.app.features.profile.view.ProfileScreen
import com.mathgate.app.ui.theme.AppScaffold

enum class Screens(val route: String, val title: String, val icon: ImageVector) {
    OgeHome("oge_home", "Экзамены", Icons.Default.School),
    FreemodeHome("freemode_home", "Свободный", Icons.Default.Keyboard),
    Profile("profile", "Профиль", Icons.Default.Person)
}

@Composable
fun MainPage(
    rootNavController: NavController,
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

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
            modifier = Modifier.fillMaxSize().padding(bottom = contentPadding.calculateBottomPadding()),
            navController = tabNavController,
            startDestination = Screens.FreemodeHome.route
        ) {
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