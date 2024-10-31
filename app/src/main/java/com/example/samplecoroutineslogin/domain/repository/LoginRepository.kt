package com.example.samplecoroutineslogin.domain.repository

import com.example.samplecoroutineslogin.helpers.Result

interface LoginRepository {
    suspend fun login(username: String, password: String): Result<Unit>
}
