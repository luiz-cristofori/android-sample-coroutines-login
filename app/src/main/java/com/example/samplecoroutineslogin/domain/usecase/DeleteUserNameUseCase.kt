package com.example.samplecoroutineslogin.domain.usecase

import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.helpers.Result

class DeleteUserNameUseCase(private val repository: UserPreferencesRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            repository.deleteUserName()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
