package com.example.samplecoroutineslogin.domain

import android.content.SharedPreferences
import com.example.samplecoroutineslogin.data.repository.UserPreferencesRepositoryImpl
import com.example.samplecoroutineslogin.domain.repository.UserPreferencesRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserPreferencesRepositoryTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private lateinit var repository: UserPreferencesRepository

    private val userNameMock = "userNameMock"

    @Before
    fun setUp() {
        repository = UserPreferencesRepositoryImpl(sharedPreferences)

        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just runs
        every { editor.remove(any()) } returns editor
    }

    @Test
    fun `given non-blank username when saveUserName then saves to SharedPreferences`() =
        runBlocking {
            repository.saveUserName(userNameMock)

            verify { editor.putString("username", userNameMock) }
            verify { editor.apply() }
        }

    @Test
    fun `given blank username when saveUserName then does not save`() = runBlocking {
        repository.saveUserName("")

        verify(exactly = 0) { editor.putString(any(), any()) }
        verify(exactly = 0) { editor.apply() }
    }

    @Test
    fun `given username in SharedPreferences when getUserName then retrieves username`() =
        runBlocking {
            every { sharedPreferences.getString("username", any()) } returns userNameMock

            val result = repository.getUserName()

            assertEquals(userNameMock, result)
            verify { sharedPreferences.getString("username", "") }
        }

    @Test
    fun `when deleteUserName then removes username from SharedPreferences`() = runBlocking {
        repository.deleteUserName()

        verify { editor.remove("username") }
        verify { editor.apply() }
    }

}
