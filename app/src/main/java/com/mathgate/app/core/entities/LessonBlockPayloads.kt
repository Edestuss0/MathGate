package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
sealed class LessonBlockPayload

@Serializable
data class TextLessonPayload(
    val text: String
) : LessonBlockPayload()

@Serializable
data class SvgLessonPayload(
    val svg: String
) : LessonBlockPayload()

@Serializable
data class FormulaLessonPayload(
    val formula: String
) : LessonBlockPayload()

@Serializable
data class InputQuestionLessonPayload(
    val question: String,
    val answer: String
) : LessonBlockPayload()

@Serializable
data class ChoiceQuestionLessonPayload(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
) : LessonBlockPayload()