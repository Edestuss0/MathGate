package com.mathgate.app.features.trigonometry.domain.entity

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

data class TrigonometryTableValue(
    val degree: Int,
    val latexRadian: String,
    val latexSin: String?,
    val latexCos: String?,
    val latexTg: String?
) {
    val radian: Double get() = Math.toRadians(degree.toDouble())
    val sin: Double get() = BigDecimal(sin(radian)).setScale(2, RoundingMode.HALF_UP).toDouble()
    val cos: Double get() = BigDecimal(cos(radian)).setScale(2, RoundingMode.HALF_UP).toDouble()
    val tg: Double get() = BigDecimal(tan(radian)).setScale(2, RoundingMode.HALF_UP).toDouble()
}
