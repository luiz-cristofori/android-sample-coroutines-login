package com.example.samplecoroutineslogin.domain.repository

interface UserPreferencesRepository {
    suspend fun saveUserName(userName: String)
    suspend fun getUserName(): String
    suspend fun deleteUserName()
}
