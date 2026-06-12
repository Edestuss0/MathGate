package com.mathgate.app.core.entities

data class User(
    val username: String = "Загрузка",
    val level: Int = 0,
    val experience: Int = 0,
    val best_streak: Int = 0,
    val registered: Boolean = false,
    val current_campaign: Int = 1,
    val current_grade: Int? = null
)