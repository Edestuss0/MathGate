package com.mathgate.app.shared.user.domain.entity

data class User(
    val username: String = "Загрузка",
    val level: Int = 0,
    val levelUpLimit: Int = 1000,
    val experience: Int = 0,
    val bestStreak: Int = 0,
    val currentStreak: Int = 0,
    val freemodeData: List<FreemodeStatsItem> = emptyList(),
    val examData: List<ExamStatsItem> = emptyList(),
    val registered: Boolean = false,

    ) {
    val freemodeSuccessRate: Int
        get() = if (freemodeData.isEmpty()) 0 else (freemodeData.count { it.correct } * 100 / freemodeData.size)

    val examSuccessRate: Int
        get() = if (examData.isEmpty()) 0 else (examData.count { it.correct } * 100 / examData.size)
}