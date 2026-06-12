package com.mathgate.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.svg.SvgDecoder
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig
import com.mathgate.app.core.entities.ChoiceQuestionLessonPayload
import com.mathgate.app.core.entities.FormulaLessonPayload
import com.mathgate.app.core.entities.InputQuestionLessonPayload
import com.mathgate.app.core.entities.SvgLessonPayload
import com.mathgate.app.core.entities.TextLessonPayload
import okhttp3.internal.trimSubstring

@Composable
fun TextPayloadItem(
    payload: TextLessonPayload
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = payload.text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun FormulaPayloadItem(
    payload: FormulaLessonPayload
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Latex(
            latex = payload.formula,
            config = LatexConfig(
                fontSize = 32.sp,
            ),
        )

    }
}

@Composable
fun SvgPayloadItem(
    payload: SvgLessonPayload,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    AsyncImage(
        model = payload.svg,
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}


@Composable
fun InputQuestionPayloadItem(
    payload: InputQuestionLessonPayload,
    isError: Boolean,
    onRightAnswer: () -> Unit,
    onWrongAnswer: () -> Unit,
    isLocked: Boolean
) {
    var answerInput by remember { mutableStateOf<String>("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = payload.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            value = answerInput,
            isError = isError,
            label = { Text("Введите свой ответ") },
            onValueChange = {newValue ->
                answerInput = newValue
            }
        )

        Button(
            enabled = isLocked,
            onClick = {
                if (payload.answer == answerInput.trim()) {
                    onRightAnswer()
                } else {
                    answerInput = ""
                    onWrongAnswer()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(text = "Ответить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ChoiceQuestionPayloadItem(
    payload: ChoiceQuestionLessonPayload,
    onRightAnswer: () -> Unit,
    onWrongAnswer: () -> Unit,
    isLocked: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выберите правильный ответ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        Text(
            text = payload.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        payload.answers.forEach { answer ->
            Button(
                enabled = isLocked,
                onClick = {
                    if (payload.answers.indexOf(answer) == payload.correctAnswerIndex) {
                        onRightAnswer()
                    } else {
                        onWrongAnswer()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(text = answer, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

    }
}