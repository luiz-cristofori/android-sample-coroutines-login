package com.example.samplecoroutineslogin.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplecoroutineslogin.R
import com.example.samplecoroutineslogin.data.exception.GenericErrorException
import com.example.samplecoroutineslogin.data.exception.InvalidCredentialsException
import com.example.samplecoroutineslogin.domain.usecase.DeleteUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.GetUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.LoginUseCase
import com.example.samplecoroutineslogin.domain.usecase.SaveUserNameUseCase
import com.example.samplecoroutineslogin.helpers.MutableSingleLiveEvent
import com.example.samplecoroutineslogin.helpers.Result
import com.example.samplecoroutineslogin.helpers.asLiveData
import com.example.samplecoroutineslogin.presentation.action.LoginAction
import com.example.samplecoroutineslogin.presentation.effect.LoginUiEffect
import com.example.samplecoroutineslogin.presentation.state.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val deleteUserNameUseCase: DeleteUserNameUseCase
) : ViewModel() {

    private val _state = MutableLiveData<LoginState>(LoginState.Loading)
    val state = _state.asLiveData()

    private val _effect = MutableSingleLiveEvent<LoginUiEffect>()
    val effect = _effect.asSingleLiveEvent()

    init {
        handleUserNameCache()
    }

    fun sendAction(action: LoginAction.Action) {
        when (action) {
            is LoginAction.Action.OnLoginButtonClick -> {
                handleLogin()
            }

            is LoginAction.Action.OnPasswordInputChange -> {
                _state.value =
                    LoginState.Resume(
                        uiModel = getCurrentUiModel().copy(
                            passwordInputText = action.text
                        )
                    )
            }

            is LoginAction.Action.OnRememberUserNameChecked -> {
                handleOnRememberUserNameChecked(action.isChecked)
            }

            is LoginAction.Action.OnUserNameInputChange -> {
                _state.value =
                    LoginState.Resume(
                        uiModel = getCurrentUiModel().copy(
                            userNameInputText = action.text
                        )
                    )
            }

            is LoginAction.Action.OnTryAgainClick -> {
                _state.value = LoginState.Resume(uiModel = getCurrentUiModel())
            }
        }
    }

    private fun handleLogin() {
        startRequestLoading()
        viewModelScope.launch {
            if (getCurrentUiModel().isRememberLoginChecked) {
                saveUserNameUseCase(getCurrentUiModel().userNameInputText)
            }
            val result = loginUseCase(
                userName = getCurrentUiModel().userNameInputText,
                password = getCurrentUiModel().passwordInputText
            )
            when (result) {
                is Result.Error -> {
                    stopRequestLoading()
                    when (result.exception) {
                        is GenericErrorException -> {
                            _state.value = LoginState.Error(getCurrentUiModel())
                        }

                        is InvalidCredentialsException -> {
                            _effect.value =
                                LoginUiEffect.DisplaySnackBarError(R.string.login_screen_login_error_text)
                        }
                    }
                }

                is Result.Success -> {
                    stopRequestLoading()
                    _effect.value = LoginUiEffect.DisplaySnackBarSuccess
                }
            }
        }
    }

    private fun handleOnRememberUserNameChecked(checked: Boolean) {
        viewModelScope.launch {
            if (checked) {
                saveUserNameUseCase(getCurrentUiModel().userNameInputText)
            } else {
                deleteUserNameUseCase()
            }
            _state.value =
                LoginState.Resume(
                    uiModel = getCurrentUiModel().copy(
                        isRememberLoginChecked = checked
                    )
                )
        }
    }

    private fun handleUserNameCache() {
        viewModelScope.launch {
            when (val result = getUserNameUseCase()) {
                is Result.Error -> {
                    _effect.value =
                        LoginUiEffect.DisplaySnackBarError(R.string.login_screen_generic_error_text)
                }

                is Result.Success -> {
                    _state.value =
                        LoginState.Resume(
                            uiModel = getCurrentUiModel().copy(
                                userNameInputText = result.data,
                                isRememberLoginChecked = result.data.isNotBlank()
                            )
                        )
                }
            }
        }
    }

    private fun startRequestLoading() {
        _state.value =
            LoginState.Resume(
                uiModel = getCurrentUiModel().copy(
                    isRequestInProgress = true
                )
            )
    }

    private fun stopRequestLoading() {
        _state.value =
            LoginState.Resume(
                uiModel = getCurrentUiModel().copy(
                    isRequestInProgress = false
                )
            )
    }

    private fun getCurrentUiModel() = checkNotNull(_state.value?.uiModel)

}
