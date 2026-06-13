package com.mathgate.app.core.entities

import kotlinx.serialization.Serializable

@Serializable
data class OgeAddDto(
    val id: Int,
    val answer: String,
    val blocks: List<OgeBlockAddDto>
)

@Serializable
data class OgeBlockAddDto(
    val type: String,
    val content: String
)
