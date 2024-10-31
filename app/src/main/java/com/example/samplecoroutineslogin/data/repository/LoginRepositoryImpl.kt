package com.example.samplecoroutineslogin.data.repository

import com.example.samplecoroutineslogin.domain.repository.LoginRepository
import com.example.samplecoroutineslogin.helpers.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

private const val MOCK_USER = "testuser"
private const val MOCK_PASSWORD = "password"

class LoginRepositoryImpl : LoginRepository {
    override suspend fun login(username: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            delay(2.seconds)
            when (username) {
                MOCK_USER -> {
                    if (password == MOCK_PASSWORD) {
                        Result.Success(Unit)
                    } else {
                        Result.Error(InvalidCredentialsException())
                    }
                }

                else -> {
                    Result.Error(GenericErrorException())
                }
            }
        }
    }
}

class InvalidCredentialsException : Exception("Invalid credentials")
class GenericErrorException : Exception("Generic Error")
