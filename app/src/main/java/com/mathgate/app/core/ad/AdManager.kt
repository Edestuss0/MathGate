package com.mathgate.app.core.ad

import android.app.Activity
import android.content.Context
import com.mathgate.app.core.app.INTERSTITIAL_AD_UNIT_ID
import com.mathgate.app.core.di.ApplicationScope
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val adUnitId = INTERSTITIAL_AD_UNIT_ID

    private val adLoader = InterstitialAdLoader(context)
    private var interstitialAd: InterstitialAd? = null

    private val _isAdLoaded = MutableStateFlow(false)
    val isAdLoaded: StateFlow<Boolean> = _isAdLoaded.asStateFlow()

    private var retryDelay = 2.seconds
    private var isLoading = false
    private var lastAdShowTimeMillis = 0L
    private val adCooldownMillis = 3 * 60 * 1000

    init {
        loadAd()
    }

    fun loadAd() {
        if (_isAdLoaded.value || isLoading) return
        isLoading = true

        scope.launch {
            try {
                val adRequest = AdRequest.Builder(adUnitId).build()

                when (val result = adLoader.loadAd(adRequest)) {
                    is InterstitialAdLoadResult.Success -> {
                        interstitialAd = result.ad
                        _isAdLoaded.value = true
                        retryDelay = 2.seconds
                    }
                    is InterstitialAdLoadResult.Failure -> {
                        clearAdState()

                        delay(retryDelay)
                        retryDelay = (retryDelay * 2).coerceAtMost(60.seconds)
                        isLoading = false
                        loadAd()
                    }
                }
            } catch (e: Exception) {
                clearAdState()
            } finally {
                isLoading = false
            }
        }
    }

    fun showAd(activity: Activity, onAdClosed: () -> Unit) {
        val ad = interstitialAd

        if (ad != null && _isAdLoaded.value) {
            ad.setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {}

                override fun onAdFailedToShow(adError: AdError) {
                    handleAdCompletion(onAdClosed)
                }

                override fun onAdDismissed() {
                    handleAdCompletion(onAdClosed)
                }

                override fun onAdClicked() {}
                override fun onAdImpression(impressionData: ImpressionData?) {}
            })
            ad.show(activity)
        } else {
            onAdClosed()
            loadAd()
        }
    }


    fun showAdWithProbability(activity: Activity, probability: Float, onAdClosed: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        val isCooldownActive = (currentTime - lastAdShowTimeMillis) < adCooldownMillis
        val isLuckyRoll = kotlin.random.Random.nextFloat() <= probability

        if (!isCooldownActive && isLuckyRoll && _isAdLoaded.value) {
            lastAdShowTimeMillis = currentTime
            showAd(activity, onAdClosed)
        } else {
            onAdClosed()
        }
    }

    private fun handleAdCompletion(onAdClosed: () -> Unit) {
        clearAdState()
        onAdClosed()
        loadAd()
    }

    private fun clearAdState() {
        interstitialAd = null
        _isAdLoaded.value = false
    }
}