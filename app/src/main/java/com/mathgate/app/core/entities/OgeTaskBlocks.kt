package com.mathgate.app.core.entities

sealed class TaskBlock

data class TextBlock(val text: String) : TaskBlock()

data class ImageBlock(val url: String) : TaskBlock()

data class FormulaBlock(val url: String) : TaskBlock()