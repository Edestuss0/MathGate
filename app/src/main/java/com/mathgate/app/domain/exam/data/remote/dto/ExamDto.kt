package com.mathgate.app.domain.exam.data.remote.dto

import com.mathgate.app.domain.exam.entity.ExamBlock
import com.mathgate.app.domain.exam.entity.ExamBlockType
import com.mathgate.app.domain.exam.entity.ExamQuestion
import kotlinx.serialization.Serializable

@Serializable
data class ExamQuestionDto (
    val id: Int,
    val answer: String,
    val blocks: List<ExamBlockDto>,
    val solutionBlocks: List<ExamBlockDto>
)

@Serializable
data class ExamBlockDto (
    val type: String,
    val content: String,
)

fun ExamBlockDto.toDomain(): ExamBlock {
    val type = when (this.type) {
        "TEXT" -> ExamBlockType.TEXT
        "IMAGE" -> ExamBlockType.IMAGE
        "FORMULA" -> ExamBlockType.FORMULA
        else -> ExamBlockType.TEXT
    }
    return ExamBlock(
        type = type,
        content = this.content
    )
}

fun ExamQuestionDto.toDomain(): ExamQuestion {
    return ExamQuestion(
        id = id,
        answer = answer,
        blocks = blocks.map { it.toDomain() },
        solutionBlocks = solutionBlocks.map { it.toDomain() }
    )
}
