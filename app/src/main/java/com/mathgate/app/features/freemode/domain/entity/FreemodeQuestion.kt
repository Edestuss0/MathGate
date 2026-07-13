package com.mathgate.app.features.freemode.domain.entity

data class FreemodeQuestion(
    val answer: String,
    val blocks: List<FreemodeQuestionBlock>,
)
