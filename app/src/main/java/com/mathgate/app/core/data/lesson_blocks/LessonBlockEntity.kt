package com.mathgate.app.core.data.lesson_blocks

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mathgate.app.core.data.lesson_pages.LessonPageEntity
import com.mathgate.app.core.entities.ChoiceQuestionLessonPayload
import com.mathgate.app.core.entities.FormulaLessonPayload
import com.mathgate.app.core.entities.InputQuestionLessonPayload
import com.mathgate.app.core.entities.LessonBlock
import com.mathgate.app.core.entities.LessonBlockPayload
import com.mathgate.app.core.entities.LessonBlockType
import com.mathgate.app.core.entities.SvgLessonPayload
import com.mathgate.app.core.entities.TextLessonPayload
import kotlinx.serialization.json.Json

@Entity(
    tableName = "lesson_blocks",
    foreignKeys = [
        ForeignKey(
            entity = LessonPageEntity::class,
            parentColumns = ["id"],
            childColumns = ["pageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pageId")]
)
data class LessonBlockEntity(
    @PrimaryKey
    val id: Int,
    val pageId: Int,
    val blockType: String,
    val orderIndex: Int,
    val payload: String
)

fun LessonBlockEntity.toDomain(): LessonBlock {
    val type = when (blockType) {
        "TEXT" -> LessonBlockType.TEXT
        "SVG" -> LessonBlockType.SVG
        "FORMULA" -> LessonBlockType.FORMULA
        "INPUT_QUESTION" -> LessonBlockType.INPUT_QUESTION
        "CHOICE_QUESTION" -> LessonBlockType.CHOICE_QUESTION
        else -> LessonBlockType.TEXT
    }

    val payload = when (type) {
        LessonBlockType.TEXT ->
            Json.decodeFromString<TextLessonPayload>(payload)

        LessonBlockType.SVG ->
            Json.decodeFromString<SvgLessonPayload>(payload)

        LessonBlockType.FORMULA ->
            Json.decodeFromString<FormulaLessonPayload>(payload)

        LessonBlockType.INPUT_QUESTION -> {
            Json.decodeFromString<InputQuestionLessonPayload>(payload)
        }

        LessonBlockType.CHOICE_QUESTION ->
            Json.decodeFromString<ChoiceQuestionLessonPayload>(payload)
    }

    return LessonBlock(
        id = id,
        blockType = type,
        orderIndex = orderIndex,
        payload = payload
    )
}