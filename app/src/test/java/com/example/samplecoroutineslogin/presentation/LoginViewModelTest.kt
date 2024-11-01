package com.example.samplecoroutineslogin.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samplecoroutineslogin.R
import com.example.samplecoroutineslogin.data.exception.GenericErrorException
import com.example.samplecoroutineslogin.data.exception.InvalidCredentialsException
import com.example.samplecoroutineslogin.domain.usecase.DeleteUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.GetUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.LoginUseCase
import com.example.samplecoroutineslogin.domain.usecase.SaveUserNameUseCase
import com.example.samplecoroutineslogin.helpers.Result
import com.example.samplecoroutineslogin.presentation.action.LoginAction
import com.example.samplecoroutineslogin.presentation.effect.LoginUiEffect
import com.example.samplecoroutineslogin.presentation.state.LoginState
import com.example.samplecoroutineslogin.presentation.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    private val loginUseCase: LoginUseCase = mockk()
    private val getUserNameUseCase: GetUserNameUseCase = mockk()
    private val saveUserNameUseCase: SaveUserNameUseCase = mockk()
    private val deleteUserNameUseCase: DeleteUserNameUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            getUserNameUseCase = getUserNameUseCase,
            saveUserNameUseCase = saveUserNameUseCase,
            deleteUserNameUseCase = deleteUserNameUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when userName exists then sets state to Resume with userName`() =
        runBlocking {
            val userName = "testuser"
            coEvery { getUserNameUseCase() } returns Result.Success(userName)

            viewModel = LoginViewModel(
                loginUseCase = loginUseCase,
                getUserNameUseCase = getUserNameUseCase,
                saveUserNameUseCase = saveUserNameUseCase,
                deleteUserNameUseCase = deleteUserNameUseCase
            )

            val state = viewModel.state.value
            assertThat(state, instanceOf(LoginState.Resume::class.java))
            assertEquals((state as LoginState.Resume).uiModel.userNameInputText, userName)
        }

    @Test
    fun `given successful login when username is correct then triggers DisplaySnackBarSuccess effect`() =
        runBlocking {
            coEvery { loginUseCase(any(), any()) } returns Result.Success(Unit)
            coEvery { saveUserNameUseCase(any()) } returns Result.Success(Unit)

            viewModel.sendAction(LoginAction.Action.OnLoginButtonClick("username", "password"))

            val effect = viewModel.effect.value
            assertEquals(effect, LoginUiEffect.DisplaySnackBarSuccess)
        }

    @Test
    fun `given invalid credentials when username is wrong triggers DisplaySnackBarError effect`() =
        runBlocking {
            coEvery {
                loginUseCase(
                    any(),
                    any()
                )
            } returns Result.Error(InvalidCredentialsException())

            viewModel.sendAction(LoginAction.Action.OnLoginButtonClick("username", "password"))

            val effect = viewModel.effect.value
            assertEquals(
                effect,
                LoginUiEffect.DisplaySnackBarError(R.string.login_screen_login_error_text)
            )
        }

    @Test
    fun `given generic error when log in then sets state to Error`() = runBlocking {
        coEvery { loginUseCase(any(), any()) } returns Result.Error(GenericErrorException())

        viewModel.sendAction(LoginAction.Action.OnLoginButtonClick("username", "password"))

        val state = viewModel.state.value
        assertThat(state, instanceOf(LoginState.Error::class.java))
    }

    @Test
    fun `given remember me when unchecked deletes userName and updates state`() = runBlocking {
        coEvery { deleteUserNameUseCase() } returns Result.Success(Unit)

        viewModel.sendAction(LoginAction.Action.OnRememberUserNameChecked(false))

        val state = viewModel.state.value
        assertThat(state, instanceOf(LoginState.Resume::class.java))
        assertFalse((state as LoginState.Resume).uiModel.isRememberLoginChecked)
    }
}
