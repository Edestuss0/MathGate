package com.mathgate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.mathgate.app.core.ad.AdManager
import com.mathgate.app.core.navigation.view.AppNavigation
import com.mathgate.app.shared.notification.presentation.view.NotificationPermissionRequester
import com.mathgate.app.ui.theme.MathGateTheme
import com.yandex.mobile.ads.common.YandexAds
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    @Inject lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = Firebase.analytics
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathGateTheme() {
                AppNavigation(adManager)
            }
        }
    }
}

