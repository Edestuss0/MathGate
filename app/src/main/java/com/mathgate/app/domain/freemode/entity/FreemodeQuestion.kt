package com.mathgate.app.domain.freemode.entity

data class FreemodeQuestion(
    val answer: String,
    val blocks: List<FreemodeQuestionBlock>,
)
