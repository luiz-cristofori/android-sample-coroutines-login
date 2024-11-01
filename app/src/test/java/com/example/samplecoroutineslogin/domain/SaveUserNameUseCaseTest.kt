package com.example.samplecoroutineslogin.domain

import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.domain.usecase.SaveUserNameUseCase
import com.example.samplecoroutineslogin.helpers.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveUserNameUseCaseTest {

    private val repository: UserPreferencesRepository = mockk()
    private lateinit var saveUserNameUseCase: SaveUserNameUseCase

    @Before
    fun setUp() {
        saveUserNameUseCase = SaveUserNameUseCase(repository)
    }

    @Test
    fun `given valid username when invoke then returns Success`() = runBlocking {
        val userName = "testuser"
        coEvery { repository.saveUserName(userName) } returns Unit

        val result = saveUserNameUseCase(userName)

        assertThat(result, instanceOf(Result.Success::class.java))
    }

    @Test
    fun `given repository throws exception when invoke then returns Error`() = runBlocking {
        val userName = "testuser"
        val exception = Exception("Failed to save username")

        coEvery { repository.saveUserName(userName) } throws exception

        val result = saveUserNameUseCase(userName)

        assertThat(result, instanceOf(Result.Error::class.java))
        assertEquals((result as Result.Error).exception, exception)
    }
}
