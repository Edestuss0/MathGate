package com.mathgate.app.core.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mathgate.app.core.entities.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

class UserRepository @Inject constructor(
   @ApplicationContext private val context: Context
) {

    private val dataStore = context.applicationContext.dataStore

    fun getProfile(): Flow<User> = dataStore.data.map { preferences ->
        User(
            username = preferences[USERNAME] ?: "",
            level = preferences[LEVEL] ?: 0,
            experience = preferences[EXPERIENCE] ?: 0,
            best_streak = preferences[BEST_STREAK] ?: 0,
            registered = preferences[REGISTERED] ?: false,
            current_campaign = preferences[CURRENT_CAMPAIGN] ?: 1,
        )
    }

    suspend fun register(name: String) {
        require(name.isNotBlank()) {"Имя пользователя не может быть пустым"}

        dataStore.edit { preferences ->
            preferences[USERNAME] = name
            preferences[REGISTERED] = true
            preferences[LEVEL] = 0
            preferences[EXPERIENCE] = 0
            preferences[BEST_STREAK] = 0
            preferences[CURRENT_CAMPAIGN] = 1
        }

        println("Успешная регистрация")
    }

    suspend fun deleteAccount() {
        dataStore.edit { preferences ->
            preferences[USERNAME] = ""
            preferences[LEVEL] = 0
            preferences[EXPERIENCE] = 0
            preferences[BEST_STREAK] = 0
            preferences[REGISTERED] = false
            preferences[CURRENT_CAMPAIGN] = 1
        }
    }

    suspend fun addExperience(value: Int) {
            dataStore.edit { preferences ->
                val currentExp: Int = preferences[EXPERIENCE] ?: 0
                val currentLevel: Int = preferences[LEVEL] ?: 0
                var finalValue: Int = 0

                if ((currentExp + value) >= 1000) {
                    finalValue = (currentExp + value) - 1000
                    preferences[EXPERIENCE] = finalValue
                    preferences[LEVEL] = currentLevel + 1
                } else {
                    finalValue = currentExp + value
                    preferences[EXPERIENCE] = finalValue
                }

            }
    }

    suspend fun updateStreak(streak: Int) {
        dataStore.edit { preferences ->
            val currentBestStreak = preferences[BEST_STREAK] ?: 0
            if (streak < currentBestStreak) return@edit
            preferences[BEST_STREAK] = streak
        }
    }

    suspend fun completeCampaign() {
        dataStore.edit { preferences ->
            val currentCampaign = preferences[CURRENT_CAMPAIGN] ?: 1
            val currentExp = preferences[EXPERIENCE] ?: 0
            preferences[CURRENT_CAMPAIGN] = currentCampaign + 1
            preferences[EXPERIENCE] = currentExp + (currentCampaign * 10)
        }
    }

}


