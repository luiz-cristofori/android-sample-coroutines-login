package com.example.samplecoroutineslogin.domain

import com.example.samplecoroutineslogin.data.exception.GenericErrorException
import com.example.samplecoroutineslogin.data.exception.InvalidCredentialsException
import com.example.samplecoroutineslogin.data.repository.LoginRepositoryImpl
import com.example.samplecoroutineslogin.domain.repository.LoginRepository
import com.example.samplecoroutineslogin.helpers.Result
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class LoginRepositoryTest {

    private lateinit var repository: LoginRepository

    private val userNameMock = "testuser"
    private val passwordMock = "password"

    @Before
    fun setUp() {
        repository = LoginRepositoryImpl()
    }

    @Test
    fun `given valid credentials when login then returns Success`() = runBlocking {
        val result = repository.login(userNameMock, passwordMock)

        assertThat(result, instanceOf(Result.Success::class.java))
    }

    @Test
    fun `given invalid password when login then returns InvalidCredentialsException Error`() =
        runBlocking {
            val result = repository.login(userNameMock, "wrongpassword")

            assertThat(result, instanceOf(Result.Error::class.java))
            assertThat(
                (result as Result.Error).exception,
                instanceOf(InvalidCredentialsException::class.java)
            )
        }

    @Test
    fun `given unknown username when login then returns GenericErrorException Error`() =
        runBlocking {
            val result = repository.login("unknownuser", passwordMock)

            assertThat(result, instanceOf(Result.Error::class.java))
            assertThat(
                (result as Result.Error).exception,
                instanceOf(GenericErrorException::class.java)
            )
        }
}
