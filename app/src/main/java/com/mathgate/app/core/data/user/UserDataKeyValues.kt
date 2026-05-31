package com.mathgate.app.core.data.user

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val USERNAME = stringPreferencesKey("username")
val LEVEL = intPreferencesKey("level")
val EXPERIENCE = intPreferencesKey("experience")
val MONEY = intPreferencesKey("money")
val BEST_STREAK = intPreferencesKey("best_streak")
val REGISTERED = booleanPreferencesKey("registered")
val CURRENT_CAMPAIGN = intPreferencesKey("current_campaign")