package com.mathgate.app.features.trigonometry.presentation.viewmodel

import com.mathgate.app.features.trigonometry.domain.entity.TrigonometryTableValue
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

data class TrigonometryState(
    val currentAngle: CurrentAngle = CurrentAngle(),
    val currentTableAngle: TrigonometryTableValue? = null,
    val isCalculated: Boolean = false
)

data class CurrentAngle(
    val degree: Int = 0,
) {
    val radian: Double get() = Math.toRadians(degree.toDouble())
    val sin: Double get() = BigDecimal(sin(radian)).setScale(2, RoundingMode.HALF_UP).toDouble()
    val cos: Double get() = BigDecimal(cos(radian)).setScale(2, RoundingMode.HALF_UP).toDouble()
    val tg: Double get() = BigDecimal(tan(radian)).setScale(2, RoundingMode.HALF_UP).toDouble()

}

sealed class TrigonometryEvent {
    data class SelectAngle(val angle: Double) : TrigonometryEvent()
    data object OnCalculate : TrigonometryEvent()
    data object ToCircle : TrigonometryEvent()
    data object BackClick : TrigonometryEvent()
}

sealed class TrigonometryEffect {
    data object NavigateBack : TrigonometryEffect()
}