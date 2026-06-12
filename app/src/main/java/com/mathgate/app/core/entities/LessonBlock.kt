package com.mathgate.app.core.entities
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

import kotlinx.serialization.Serializable

@Serializable
data class LessonBlock(
    val id: Int,
    val blockType: LessonBlockType,
    val orderIndex: Int,
    val payload: LessonBlockPayload
)


@Serializable
data class LessonBlockDto(
    val id: Int,
    val blockType: LessonBlockType,
    val orderIndex: Int,
    val payload: JsonObject
) {
    fun toBlock(): LessonBlock {
        val payloadObj = payload

        val payloadModel = when (blockType) {
            LessonBlockType.TEXT ->
                Json.decodeFromJsonElement<TextLessonPayload>(payloadObj)

            LessonBlockType.SVG ->
                Json.decodeFromJsonElement<SvgLessonPayload>(payloadObj)

            LessonBlockType.FORMULA ->
                Json.decodeFromJsonElement<FormulaLessonPayload>(payloadObj)

            LessonBlockType.INPUT_QUESTION ->
                Json.decodeFromJsonElement<InputQuestionLessonPayload>(payloadObj)

            LessonBlockType.CHOICE_QUESTION ->
                Json.decodeFromJsonElement<ChoiceQuestionLessonPayload>(payloadObj)
        }

        return LessonBlock(
            id = id,
            blockType = blockType,
            orderIndex = orderIndex,
            payload = payloadModel
        )
    }
}