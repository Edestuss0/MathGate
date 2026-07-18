package com.mathgate.app.core.navigation.utils

import android.app.Activity
import androidx.navigation.NavController
import com.mathgate.app.core.ad.AdManager

fun NavController.navigateWithAd(
    route: String,
    activity: Activity?,
    adManager: AdManager,
    probability: Float = 0.35f
) {
    if (activity != null) {
        adManager.showAdWithProbability(activity, probability) {
            this.navigate(route)
        }
    } else {
        this.navigate(route)
    }
}