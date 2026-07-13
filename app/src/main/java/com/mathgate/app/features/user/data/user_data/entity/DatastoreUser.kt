package com.mathgate.app.features.user.data.user_data.entity

data class DatastoreUser(
    val username: String,
    val level: Int,
    val experience: Int,
    val bestStreak: Int,
    val currentStreak: Int,
    val registered: Boolean = false,
)
