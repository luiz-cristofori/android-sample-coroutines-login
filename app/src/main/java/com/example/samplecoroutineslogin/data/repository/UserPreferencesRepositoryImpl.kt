package com.example.samplecoroutineslogin.data.repository

import android.content.SharedPreferences
import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserPreferencesRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    UserPreferencesRepository {
    companion object {
        private const val USERNAME_KEY = "username"
    }

    override suspend fun saveUserName(userName: String) {
        withContext(Dispatchers.IO) {
            if (userName.isNotBlank()) {
                sharedPreferences.edit().putString(USERNAME_KEY, userName).apply()
            }
        }
    }

    override suspend fun getUserName(): String {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(USERNAME_KEY, "").orEmpty()
        }
    }

    override suspend fun hasUserNamePrefs(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(USERNAME_KEY, "").isNullOrBlank().not()
        }
    }

    override suspend fun deleteUserName() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(USERNAME_KEY).apply()
        }
    }
}
