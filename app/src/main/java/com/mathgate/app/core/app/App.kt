package com.mathgate.app.core.app

import android.app.Application
import com.yandex.mobile.ads.common.YandexAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        YandexAds.initialize(this) {
        }
    }
}