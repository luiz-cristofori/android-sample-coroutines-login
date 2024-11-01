package com.example.samplecoroutineslogin.domain

import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.domain.usecase.GetUserNameUseCase
import com.example.samplecoroutineslogin.helpers.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUserNameUseCaseTest {

    private val repository: UserPreferencesRepository = mockk()
    private lateinit var useCase: GetUserNameUseCase

    @Before
    fun setUp() {
        useCase = GetUserNameUseCase(repository)
    }

    @Test
    fun `when invoke then returns Success with username`() = runBlocking {
        val expectedUserName = "testuser"

        coEvery { repository.getUserName() } returns expectedUserName

        val result = useCase()

        assertThat(result, instanceOf(Result.Success::class.java))
        assertEquals((result as Result.Success).data, expectedUserName)
    }

    @Test
    fun `when repository throws exception then returns Error`() = runBlocking {
        val exception = Exception("Failed to get username")

        coEvery { repository.getUserName() } throws exception

        val result = useCase()

        assertThat(result, instanceOf(Result.Error::class.java))
        assertEquals((result as Result.Error).exception, exception)
    }
}
