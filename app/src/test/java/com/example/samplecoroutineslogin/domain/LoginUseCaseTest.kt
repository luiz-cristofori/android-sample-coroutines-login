package com.example.samplecoroutineslogin.domain

import com.example.samplecoroutineslogin.data.exception.GenericErrorException
import com.example.samplecoroutineslogin.data.exception.InvalidCredentialsException
import com.example.samplecoroutineslogin.domain.repository.LoginRepository
import com.example.samplecoroutineslogin.domain.usecase.LoginUseCase
import com.example.samplecoroutineslogin.helpers.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private val repository: LoginRepository = mockk()
    private lateinit var useCase: LoginUseCase

    @Before
    fun setUp() {
        useCase = LoginUseCase(repository)
    }

    @Test
    fun `given valid credentials when invoke then returns Success`() = runBlocking {
        val userName = "testuser"
        val password = "password"

        coEvery { repository.login(userName, password) } returns Result.Success(Unit)

        val result = useCase(userName, password)

        assertThat(result, instanceOf(Result.Success::class.java))
    }

    @Test
    fun `given invalid credentials when invoke then returns InvalidCredentialsException Error`() =
        runBlocking {
            val userName = "testuser"
            val password = "wrongpassword"

            coEvery { repository.login(userName, password) } returns Result.Error(
                InvalidCredentialsException()
            )

            val result = useCase(userName, password)

            assertThat(result, instanceOf(Result.Error::class.java))
            assertThat(
                (result as Result.Error).exception,
                instanceOf(InvalidCredentialsException::class.java)
            )
        }

    @Test
    fun `given unknown username when invoke then returns GenericErrorException Error`() =
        runBlocking {
            val userName = "unknownuser"
            val password = "password"

            coEvery { repository.login(userName, password) } returns Result.Error(
                GenericErrorException()
            )

            val result = useCase(userName, password)

            assertThat(result, instanceOf(Result.Error::class.java))
            assertThat(
                (result as Result.Error).exception,
                instanceOf(GenericErrorException::class.java)
            )
        }
}
