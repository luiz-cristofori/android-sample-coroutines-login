package com.example.samplecoroutineslogin.domain.usecase

import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.helpers.Result

class GetUserNameUseCase(private val repository: UserPreferencesRepository) {
    suspend operator fun invoke(): Result<String> {
        return try {
            Result.Success(repository.getUserName())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
