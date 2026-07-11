package com.mathgate.app.domain.user.data.user_data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mathgate.app.domain.user.data.user_data.entity.BEST_STREAK
import com.mathgate.app.domain.user.data.user_data.entity.CURRENT_STREAK
import com.mathgate.app.domain.user.data.user_data.entity.DatastoreUser
import com.mathgate.app.domain.user.data.user_data.entity.EXPERIENCE
import com.mathgate.app.domain.user.data.user_data.entity.LEVEL
import com.mathgate.app.domain.user.data.user_data.entity.REGISTERED
import com.mathgate.app.domain.user.data.user_data.entity.USERNAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

class UserSource @Inject constructor(
   @ApplicationContext private val context: Context
) {

    private val dataStore = context.applicationContext.dataStore

    fun getProfile(): Flow<DatastoreUser> = dataStore.data.map { preferences ->
        DatastoreUser(
            username = preferences[USERNAME] ?: "",
            level = preferences[LEVEL] ?: 0,
            experience = preferences[EXPERIENCE] ?: 0,
            bestStreak = preferences[BEST_STREAK] ?: 0,
            registered = preferences[REGISTERED] ?: false,
            currentStreak = preferences[CURRENT_STREAK] ?: 0
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
            preferences[CURRENT_STREAK] = 0
        }

        println("Успешная регистрация")
    }

    suspend fun deleteAccount() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME)
            preferences.remove(LEVEL)
            preferences.remove(EXPERIENCE)
            preferences.remove(BEST_STREAK)
            preferences.remove(REGISTERED)
            preferences.remove(CURRENT_STREAK)
        }
    }

    suspend fun updateStreak() {
        dataStore.edit { preferences ->
            val currentStreak = preferences[CURRENT_STREAK] ?: 0
            val currentBestStreak = preferences[BEST_STREAK] ?: 0
            if (currentStreak + 1 > currentBestStreak) {
                preferences[BEST_STREAK] = currentBestStreak + 1
            }
            preferences[CURRENT_STREAK] = currentStreak + 1
        }
    }

    suspend fun failStreak() {
        dataStore.edit { preferences ->
            preferences[CURRENT_STREAK] = 0
        }
    }

}


