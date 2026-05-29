package com.mathgate.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mathgate.app.features.campaign.CampaignHomeScreen
import com.mathgate.app.features.freemode.FreemodeHomeScreen
import com.mathgate.app.features.profile.ProfileScreen

enum class Screens(val route: String, val title: String, val icon: ImageVector) {
    CampaignHome("campaign_home", "Кампания", Icons.Default.Flag),
    FreemodeHome("freemode_home", "Свободный", Icons.Default.Keyboard),
    Profile("profile", "Профиль", Icons.Default.Person)
}

@Composable
fun MainPage(
    rootNavController: NavController
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
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
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            navController = tabNavController,
            startDestination = Screens.FreemodeHome.route
        ) {

            composable(Screens.CampaignHome.route) {
                CampaignHomeScreen(onStartButtonClick = { rootNavController.navigate("campaign_play") })
            }

            composable(Screens.FreemodeHome.route) {
                FreemodeHomeScreen(
                    onStartButtonClick = { difficulty ->

                        rootNavController.navigate(
                            "play/$difficulty"
                        )
                    }
                )
            }


            composable(Screens.Profile.route) {
                ProfileScreen()
            }
        }
    }
}