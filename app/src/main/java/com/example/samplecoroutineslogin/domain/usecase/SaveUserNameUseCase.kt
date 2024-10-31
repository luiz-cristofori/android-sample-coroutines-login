package com.example.samplecoroutineslogin.domain.usecase

import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.helpers.Result

class SaveUserNameUseCase(private val repository: UserPreferencesRepository) {
    suspend operator fun invoke(userName: String): Result<Unit> {
        return try {
            repository.saveUserName(userName)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
