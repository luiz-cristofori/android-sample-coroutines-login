package com.example.samplecoroutineslogin.domain

import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import com.example.samplecoroutineslogin.domain.usecase.DeleteUserNameUseCase
import com.example.samplecoroutineslogin.helpers.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DeleteUserNameUseCaseTest {

    private val repository: UserPreferencesRepository = mockk()
    private lateinit var useCase: DeleteUserNameUseCase

    @Before
    fun setUp() {
        useCase = DeleteUserNameUseCase(repository)
    }

    @Test
    fun `when invoke then returns Success`() = runBlocking {
        coEvery { repository.deleteUserName() } returns Unit

        val result = useCase()

        assertThat(result, instanceOf(Result.Success::class.java))
    }

    @Test
    fun `when repository throws exception then returns Error`() = runBlocking {
        val exception = Exception("Failed to delete username")

        coEvery { repository.deleteUserName() } throws exception

        val result = useCase()

        assertThat(result, instanceOf(Result.Error::class.java))
        assertEquals((result as Result.Error).exception, exception)
    }
}
