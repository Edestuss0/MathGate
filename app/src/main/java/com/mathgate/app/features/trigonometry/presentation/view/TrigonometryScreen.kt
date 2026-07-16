package com.mathgate.app.features.trigonometry.presentation.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import com.mathgate.app.features.trigonometry.domain.entity.TrigonometryTableValue
import com.mathgate.app.features.trigonometry.domain.entity.trigonometryTable
import com.mathgate.app.features.trigonometry.presentation.view.updateAngle
import com.mathgate.app.features.trigonometry.presentation.viewmodel.CurrentAngle
import com.mathgate.app.features.trigonometry.presentation.viewmodel.TrigonometryEffect
import com.mathgate.app.features.trigonometry.presentation.viewmodel.TrigonometryEvent
import com.mathgate.app.features.trigonometry.presentation.viewmodel.TrigonometryState
import com.mathgate.app.features.trigonometry.presentation.viewmodel.TrigonometryViewModel
import com.mathgate.app.ui.theme.AppCard
import com.mathgate.app.ui.theme.AppScaffold
import com.mathgate.app.ui.theme.PrimaryButton
import io.ktor.util.hex
import kotlin.math.*

@Composable
fun TrigonometryScreen(
    viewmodel: TrigonometryViewModel = viewModel(),
    onBackNavigate: () -> Unit
) {
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.effects.collect { effect ->
            when (effect) {
                is TrigonometryEffect.NavigateBack -> {onBackNavigate()}
            }
        }
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        hasBackButton = true,
        onBackClick = {viewmodel.onEvent(TrigonometryEvent.BackClick)}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!state.isCalculated) {
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Latex(
                            latex = state.currentAngle.degree.toString(),
                            config = LatexConfig(
                                fontSize = 20.sp
                            )
                        )
                        state.currentTableAngle?.let {
                            Latex(
                                latex = state.currentTableAngle!!.latexRadian,
                                config = LatexConfig(
                                    fontSize = 20.sp
                                )
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                InteractiveTrigonometricCircle(
                    onAngleClick = {viewmodel.onEvent(TrigonometryEvent.SelectAngle(it))},
                    state = state
                )
                Spacer(Modifier.height(16.dp))

                PrimaryButton(
                    text = "Расчитать",
                    onClick = {viewmodel.onEvent(TrigonometryEvent.OnCalculate)}
                )
            } else {
                ValuesPreview(
                    currentAngle = state.currentAngle,
                    currentTableValue = state.currentTableAngle
                )
                Spacer(Modifier.height(16.dp))

                PrimaryButton(
                    text = "Назад",
                    onClick = {viewmodel.onEvent(TrigonometryEvent.ToCircle)}
                )
            }
        }
    }
}

@Composable
fun InteractiveTrigonometricCircle(
    modifier: Modifier = Modifier,
    onAngleClick: (angle: Double) -> Unit,
    state: TrigonometryState
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        ValuesPreview(
//            currentAngle = state.currentAngle,
//            currentTableValue = state.currentTableAngle
//        )
//
//        Spacer(Modifier.height(16.dp))

        BoxWithConstraints {
            AppCard(
                modifier = Modifier.size(maxWidth)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                val down = awaitFirstDown()
                                val centerX = size.width / 2f
                                val centerY = size.height / 2f
                                updateAngle(
                                    touch = down.position,
                                    centerX = centerX,
                                    centerY = centerY,
                                    onAngleClick = onAngleClick
                                )
                                do {
                                    val event = awaitPointerEvent()
                                    val pointer = event.changes.first()

                                    updateAngle(
                                        touch = pointer.position,
                                        centerX = centerX,
                                        centerY = centerY,
                                        onAngleClick = onAngleClick
                                    )

                                } while (pointer.pressed)
                            }
                        }
                ) {
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f
                    val radius = size.width * 0.38f

                    // Отрисовка осей координат
                    drawLine(Color.LightGray, Offset(0f, centerY), Offset(size.width, centerY), strokeWidth = 2f)
                    drawLine(Color.LightGray, Offset(centerX, 0f), Offset(centerX, size.height), strokeWidth = 2f)

                    // Отрисовка основного круга
                    drawCircle(
                        color = Color(0xFFE5E5E5),
                        radius = radius,
                        center = Offset(centerX, centerY),
                        style = Stroke(width = 3f)
                    )

                    // 3. Отрисовка закрепленных табличных точек
                    trigonometryTable.forEach { tableAngle ->
                        val pinX = (centerX + radius * cos(tableAngle.radian)).toFloat()
                        val pinY = centerY - radius * sin(tableAngle.radian).toFloat()

                        val isThisSelected = state.currentTableAngle?.degree == tableAngle.degree

                        // Рисуем точку закрепленного значения
                        drawCircle(
                            color = if (isThisSelected) Color(0xFFFF5722) else Color(0xFFB0BEC5),
                            radius = if (isThisSelected) 8.dp.toPx() else 5.dp.toPx(),
                            center = Offset(pinX, pinY)
                        )
                    }

                    // 4. Отрисовка линии текущего выбранного значения (вектора)
                    val currentX = centerX + radius * cos(state.currentAngle.radian).toFloat()
                    val currentY = centerY - radius * sin(state.currentAngle.radian).toFloat()

                    drawLine(
                        color = Color(0xFF37474F),
                        start = Offset(centerX, centerY),
                        end = Offset(currentX, currentY),
                        strokeWidth = 4f
                    )

                    // Отрисовка текущей точки-указателя
                    drawCircle(
                        color = Color(0xFFFF5722),
                        radius = 9.dp.toPx(),
                        center = Offset(currentX, currentY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = Offset(currentX, currentY)
                    )
                }
            }
        }
    }
}

@Composable
private fun ValuesPreview(
    currentAngle: CurrentAngle,
    currentTableValue: TrigonometryTableValue?
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Latex(
                    latex = currentAngle.degree.toString(),
                    config = LatexConfig(
                        fontSize = 20.sp
                    )
                )
                currentTableValue?.let {
                    Latex(
                        latex = currentTableValue.latexRadian,
                        config = LatexConfig(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Latex(
                    latex = "sin",
                    config = LatexConfig(
                        fontSize = 20.sp
                    )
                )
                Spacer(Modifier.width(8.dp))
                Latex(
                    latex = currentAngle.sin.toString(),
                    config = LatexConfig(
                        fontSize = 20.sp
                    )
                )
                currentTableValue?.latexSin?.let {
                    Spacer(Modifier.width(8.dp))
                    Latex(
                        latex = currentTableValue.latexSin,
                        config = LatexConfig(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Latex(
                    latex = "cos",
                    config = LatexConfig(
                        fontSize = 20.sp
                    )
                )
                Spacer(Modifier.width(8.dp))
                Latex(
                    latex = currentAngle.cos.toString(),
                    config = LatexConfig(
                        fontSize = 20.sp
                    )
                )
                currentTableValue?.latexCos?.let {
                    Spacer(Modifier.width(8.dp))
                    Latex(
                        latex = currentTableValue.latexCos,
                        config = LatexConfig(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
        if (currentAngle.cos.toInt() != 0) {
            Spacer(Modifier.height(16.dp))
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Latex(
                        latex = "tg",
                        config = LatexConfig(
                            fontSize = 20.sp
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Latex(
                        latex = currentAngle.tg.toString(),
                        config = LatexConfig(
                            fontSize = 20.sp
                        )
                    )
                    currentTableValue?.latexTg?.let {
                        Spacer(Modifier.width(8.dp))
                        Latex(
                            latex = currentTableValue.latexTg,
                            config = LatexConfig(
                                fontSize = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun updateAngle(
    touch: Offset,
    centerX: Float,
    centerY: Float,
    onAngleClick: (Double) -> Unit
) {
    val dx = touch.x - centerX
    val dy = centerY - touch.y

    var angle = atan2(dy, dx)
    val degrees = Math.toDegrees(angle.toDouble())

    if (angle < 0) angle += (2 * PI).toFloat()

    val tableValue = trigonometryTable.firstOrNull {
        abs(it.degree - degrees) < 4
    }

    if (tableValue != null) {
        onAngleClick(tableValue.degree.toDouble())
    } else {
        onAngleClick(Math.toDegrees(angle.toDouble()))
    }

}

@Preview(showBackground = true)
@Composable
private fun TrigonometryPreview() {
    InteractiveTrigonometricCircle(
        onAngleClick = {},
        state = TrigonometryState(
            currentAngle = CurrentAngle(52)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ValuesPreview() {
    ValuesPreview(
        currentAngle = CurrentAngle(30),
        currentTableValue = TrigonometryTableValue(30, "\\frac{\\pi}{6}", null, "\\frac{\\sqrt{3}}{2}", "\\frac{\\sqrt{3}}{3}"),
    )
}