package com.example.samplecoroutineslogin.domain.usecase

import com.example.samplecoroutineslogin.domain.repository.LoginRepository
import com.example.samplecoroutineslogin.helpers.Result

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(userName: String, password: String): Result<Unit> {
        return repository.login(userName, password)
    }
}
