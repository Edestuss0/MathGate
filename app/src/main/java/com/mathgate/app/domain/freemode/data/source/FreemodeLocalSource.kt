package com.mathgate.app.domain.freemode.data.source

import com.mathgate.app.domain.freemode.entity.FreemodeQuestion
import com.mathgate.app.domain.freemode.entity.FreemodeBlockType
import com.mathgate.app.domain.freemode.entity.FreemodeQuestionBlock
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random
import kotlin.random.nextInt

class FreemodeLocalSource {

    private data class TrigAngle(
        val degrees: Int,
        val latexRadian: String,
        val sinAnswer: String,
        val cosAnswer: String
    )

    private data class TrigValue(
        val degrees: Int,
        val radian: Double,
        val latexRadian: String,
        val latexAnswer: String
    )

    private val normalAnglesSin = listOf(
        TrigAngle(0, "0", "0", "1"),
        TrigAngle(30, "\\frac{\\pi}{6}", "0.5", "0.87"),
        TrigAngle(90, "\\frac{\\pi}{2}", "1", "0"),
        TrigAngle(150, "\\frac{5\\pi}{6}", "0.5", "-0.87"),
        TrigAngle(180, "\\pi", "0", "-1")
    )

    private val normalAnglesCos = listOf(
        TrigAngle(0, "0", "0", "1"),
        TrigAngle(60, "\\frac{\\pi}{3}", "0.87", "0.5"),
        TrigAngle(90, "\\frac{\\pi}{2}", "1", "0"),
        TrigAngle(120, "\\frac{2\\pi}{3}", "0.87", "-0.5"),
        TrigAngle(180, "\\pi", "0", "-1")
    )

    fun generateQuadraticEquation(): FreemodeQuestion {
        val x1 = ((-7..7) - 0).random()
        val x2 = ((-7..7) - x1 - 0).random()

        val a = ((-3..3) - 0).random()
        val b = -a * (x1 + x2)
        val c = a * x1 * x2

        val aTerm = when {
            a == 1 -> ""
            a == -1 -> "-"
            else -> a.toString()
        }

        val bTerm = when {
            b == 1 -> "+x"
            b == -1 -> "-x"
            b > 0 -> "+${b}x"
            b < 0 -> "${b}x"
            else -> ""
        }

        val cTerm = when {
            c > 0 -> "+$c"
            c < 0 -> "$c"
            else -> ""
        }

        val equation = "${aTerm}x^{2}$bTerm$cTerm=0"

        return FreemodeQuestion(
            answer = min(x1, x2).toString(),
            blocks = listOf(
                FreemodeQuestionBlock("Найдите наименьший корень уравнения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock(equation, FreemodeBlockType.LATEX),
            )
        )
    }

    fun generateSimpleTrigonometryEquation(): FreemodeQuestion {
        val sinEquationValues = listOf(
            TrigValue(0, 0.0, "0", "0"),
            TrigValue(30, PI/6, "\\frac{\\pi}{6}", "\\frac{1}{2}"),
            TrigValue(45, PI/4, "\\frac{\\pi}{4}", "\\frac{\\sqrt{2}}{2}"),
            TrigValue(60, PI/3, "\\frac{\\pi}{3}", "\\frac{\\sqrt{3}}{2}"),
            TrigValue(90, PI/2, "\\frac{\\pi}{2}", "1"),
            TrigValue(180, PI, "\\pi", "0"),
            TrigValue(270, (3 * PI)/3, "\\frac{3\\pi}{2}", "-1"),
        )
        val cosEquationValues = listOf(
            TrigValue(0, 0.0, "0", "1"),
            TrigValue(30, PI/6, "\\frac{\\pi}{6}", "\\frac{\\sqrt{3}}{2}"),
            TrigValue(45, PI/4, "\\frac{\\pi}{4}", "\\frac{\\sqrt{2}}{2}"),
            TrigValue(60, PI/3, "\\frac{\\pi}{3}", "\\frac{1}{2}"),
            TrigValue(90, PI/2, "\\frac{\\pi}{2}", "0"),
            TrigValue(180, PI, "\\pi", "-1"),
        )
        val tgEquationValues = listOf(
            TrigValue(0, 0.0, "0", "0"),
            TrigValue(30, PI/6, "\\frac{\\pi}{6}", "\\frac{\\sqrt{3}}{3}"),
            TrigValue(45, PI/4, "\\frac{\\pi}{4}", "1"),
            TrigValue(60, PI/3, "\\frac{\\pi}{3}", "\\sqrt{3}"),
            TrigValue(180, PI, "\\pi", "0"),
        )
        val beautifulK = listOf(-2, -1, 2, 3, 4, 6)

        val random = Random.nextInt(1..2)

        when (random) {
            1 -> {
                val (funcName, values) = listOf("sin" to sinEquationValues, "cos" to cosEquationValues, "tg" to tgEquationValues).random()
                val trig = values.random()
                return FreemodeQuestion(
                    answer = trig.degrees.toString(),
                    blocks = listOf(
                        FreemodeQuestionBlock("Найдите x, ответ дайте в градусах", FreemodeBlockType.TEXT),
                        FreemodeQuestionBlock("$funcName x = ${trig.latexAnswer}", FreemodeBlockType.LATEX),
                    )
                )
            }
            else -> {
                val (funcName, values) = listOf("sin" to sinEquationValues, "cos" to cosEquationValues, "tg" to tgEquationValues).random()
                repeat(20) {
                    val baseArg = values.filter { it.degrees != 0 }.random()
                    val k = beautifulK.random()
                    val targetDegrees = baseArg.degrees * k
                    val finalArg = values.find { it.degrees == targetDegrees }
                    if (finalArg != null) {
                        return FreemodeQuestion(
                            answer = k.toString(),
                            blocks = listOf(
                                FreemodeQuestionBlock("Найдите k", FreemodeBlockType.TEXT),
                                FreemodeQuestionBlock("$funcName(k \\cdot ${baseArg.latexRadian}) = ${finalArg.latexAnswer}", FreemodeBlockType.LATEX)
                            )
                        )
                    }
                }
                val trig = values.random()
                return FreemodeQuestion(
                    answer = "1",
                    blocks = listOf(
                        FreemodeQuestionBlock("Найдите k", FreemodeBlockType.TEXT),
                        FreemodeQuestionBlock("$funcName(k \\cdot ${trig.latexRadian}) = ${trig.latexAnswer}", FreemodeBlockType.LATEX)
                    )
                )
            }
        }
    }

    fun generateSimpleTrigonometry(): FreemodeQuestion {
        val random = Random.nextInt(1..2)

        when (random) {
            1 -> {
                val isSin = Random.nextBoolean()
                val normalAngles = if (isSin) normalAnglesSin else normalAnglesCos
                val trigAngle = normalAngles.random()

                val isDegrees = Random.nextBoolean()

                val funcName = if (isSin) "\\sin" else "\\cos"
                val answer = if (isSin) trigAngle.sinAnswer else trigAngle.cosAnswer

                val argument = if (isDegrees) {
                    "${trigAngle.degrees}^{\\circ}"
                } else {
                    trigAngle.latexRadian
                }

                val equation = "$funcName $argument"

                return FreemodeQuestion(
                    answer = formatAnswer(BigDecimal(answer)),
                    blocks = listOf(
                        FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                        FreemodeQuestionBlock(equation, FreemodeBlockType.LATEX),
                        )
                )
            }
            else -> {
                val isSin = Random.nextBoolean()
                val normalAngles = if (isSin) normalAnglesSin else normalAnglesCos
                val k = Random.nextInt(-4..4)
                val trigAngle = normalAngles.random()


                val isDegrees = Random.nextBoolean()

                val funcName = if (isSin) "\\sin" else "\\cos"
                val baseAnswer = BigDecimal(if (isSin) trigAngle.sinAnswer else trigAngle.cosAnswer)
                val answer = baseAnswer.multiply(BigDecimal(k))

                val argument = if (isDegrees) {
                    "${trigAngle.degrees}^{\\circ}"
                } else {
                    trigAngle.latexRadian
                }

                val equation = "$k$funcName $argument"

                return FreemodeQuestion(
                    answer = formatAnswer(answer),
                    blocks = listOf(
                        FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                        FreemodeQuestionBlock(equation, FreemodeBlockType.LATEX),
                    )
                )
            }
        }
    }

    fun generateSimpleFraction(): FreemodeQuestion {
        val safeDenominators = listOf(2, 4, 5, 8, 10)
        val down = safeDenominators.random()

        val up1 = Random.nextInt(-10..10)
        val up2 = ((-10..10) - up1).random()
        val isAddition = Random.nextBoolean()

        val bUp1 = BigDecimal(up1)
        val bUp2 = BigDecimal(up2)
        val bDown = BigDecimal(down)

        val result = if (isAddition) {
            bUp1.add(bUp2).divide(bDown, 4, RoundingMode.HALF_UP)
        } else {
            bUp1.subtract(bUp2).divide(bDown, 4, RoundingMode.HALF_UP)
        }

        val operator = if (isAddition) "+" else "-"
        val equation = "\\frac{$up1}{$down} $operator \\frac{$up2}{$down}"

        return FreemodeQuestion(
            answer = formatAnswer(result),
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock(equation, FreemodeBlockType.LATEX),
            )
        )
    }

    fun generateSimpleLogarithm(): FreemodeQuestion {
        val random = Random.nextInt(1..2)
        when (random) {
            1 -> {
                val base = Random.nextInt(2..6)
                val pow = Random.nextInt(1..4)
                val argument = base.toDouble().pow(pow).toInt()
                return FreemodeQuestion(
                    answer = pow.toString(),
                    blocks = listOf(
                        FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                        FreemodeQuestionBlock("\\log_{$base}$argument", FreemodeBlockType.LATEX),
                    )
                )
            }
            else -> {
                val randomNew = Random.nextInt(1..2)
                when (randomNew) {
                    1 -> {
                        val base = Random.nextInt(2..6)
                        val pow1 = Random.nextInt(1..5)
                        val argument1 = base.toDouble().pow(pow1).toInt()
                        val pow2 = Random.nextInt(1..5)
                        val argument2 = base.toDouble().pow(pow2).toInt()
                        return FreemodeQuestion(
                            answer = formatAnswer(BigDecimal(pow1 + pow2)),
                            blocks = listOf(
                                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                                FreemodeQuestionBlock("\\log_{$base}$argument1 + \\log_{$base}$argument2", FreemodeBlockType.LATEX),
                            )
                        )
                    }
                    else -> {
                        val base = Random.nextInt(2..6)
                        val pow1 = Random.nextInt(2..5)
                        val pow2 = Random.nextInt(1..<pow1)
                        val argument1 = base.toDouble().pow(pow1).toInt()
                        val argument2 = base.toDouble().pow(pow2).toInt()
                        return FreemodeQuestion(
                            answer = formatAnswer(BigDecimal(pow1 - pow2)),
                            blocks = listOf(
                                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                                FreemodeQuestionBlock("\\log_{$base}{$argument1} - \\log_{$base}{$argument2}", FreemodeBlockType.LATEX),
                            )
                        )
                    }
                }
            }
        }
    }

    fun generateArithmetic(): FreemodeQuestion {
        val first = Random.nextInt(-10..10)
        val second = Random.nextInt(-10..10)
        val random = Random.nextInt(1..3)
        val action = when (random) {1-> "\\cdot" 2-> "+" else -> "-"}
        return FreemodeQuestion(
            answer =( when (random) {1 -> first * second 2-> first + second else -> first - second}).toString(),
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("$first $action $second", FreemodeBlockType.LATEX),
            )
        )
    }

    fun generateEasyLogarithmicEquation(): FreemodeQuestion {
        val x = Random.nextInt(1..10)

        val base = listOf(2, 3, 5).random()

        val pow = when (base) {
            2 -> Random.nextInt(1..4)  // 2^4 = 16
            3 -> Random.nextInt(1..3)  // 3^3 = 27
            else -> Random.nextInt(1..2) // 5^2 = 25
        }
        val argumentValue = base.toDouble().pow(pow).toInt()

        val k = Random.nextInt(1..4)

        val b = argumentValue - k * x

        val kTerm = if (k == 1) "x" else "${k}x"
        val bTerm = when {
            b > 0 -> "+$b"
            b < 0 -> "$b"
            else -> ""
        }

        val equation = "\\log_{$base}($kTerm$bTerm) = $pow"

        return FreemodeQuestion(
            answer = x.toString(),
            blocks = listOf(
                FreemodeQuestionBlock("Найдите корень уравнения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock(equation, FreemodeBlockType.LATEX)
            )
        )
    }

    fun generateMediumLogarithmicEquation(): FreemodeQuestion {
        val x = Random.nextInt(1..10)
        val base = listOf(2, 3, 5).random()

        val k = Random.nextInt(1..5)
        val m = ((1..5) - k).random()

        val argumentValue = Random.nextInt(15..45)

        val b = argumentValue - k * x
        val n = argumentValue - m * x

        val kTerm = if (k == 1) "x" else "${k}x"
        val bTerm = when {
            b > 0 -> "+$b"
            b < 0 -> "$b"
            else -> ""
        }

        val mTerm = if (m == 1) "x" else "${m}x"
        val nTerm = when {
            n > 0 -> "+$n"
            n < 0 -> "$n"
            else -> ""
        }

        val equation = "\\log_{$base}($kTerm$bTerm) = \\log_{$base}($mTerm$nTerm)"

        return FreemodeQuestion(
            answer = x.toString(),
            blocks = listOf(
                FreemodeQuestionBlock("Найдите корень уравнения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock(equation, FreemodeBlockType.LATEX)
            )
        )
    }

    private fun formatAnswer(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return stripped.toPlainString()
    }

}