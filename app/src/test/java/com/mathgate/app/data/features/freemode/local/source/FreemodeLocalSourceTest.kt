package com.mathgate.app.data.features.freemode.local.source

import com.mathgate.app.domain.freemode.data.source.FreemodeLocalSource
import org.junit.Test

class FreemodeLocalSourceTest {
    @Test
    fun testMyFraction() {
        repeat(5) {
            val q = FreemodeLocalSource().generateSimpleFraction()
            println("TEST_OUTPUT: $q")
        }
    }
}