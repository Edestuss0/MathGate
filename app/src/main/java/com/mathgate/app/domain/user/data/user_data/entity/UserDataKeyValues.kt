package com.mathgate.app.domain.user.data.user_data.entity

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val USERNAME = stringPreferencesKey("username")
val LEVEL = intPreferencesKey("level")
val EXPERIENCE = intPreferencesKey("experience")
val BEST_STREAK = intPreferencesKey("best_streak")
val REGISTERED = booleanPreferencesKey("registered")
val CURRENT_STREAK = intPreferencesKey("current_streak")
//val FREEMODE_DATA = listPre