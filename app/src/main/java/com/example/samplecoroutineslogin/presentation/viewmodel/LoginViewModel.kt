package com.example.samplecoroutineslogin.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplecoroutineslogin.domain.usecase.DeleteUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.GetUserNameUseCase
import com.example.samplecoroutineslogin.domain.usecase.SaveUserNameUseCase
import com.example.samplecoroutineslogin.helpers.MutableSingleLiveEvent
import com.example.samplecoroutineslogin.helpers.Result
import com.example.samplecoroutineslogin.helpers.asLiveData
import com.example.samplecoroutineslogin.presentation.action.LoginAction
import com.example.samplecoroutineslogin.presentation.effect.LoginUiEffect
import com.example.samplecoroutineslogin.presentation.state.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(
    val getUserNameUseCase: GetUserNameUseCase,
    val saveUserNameUseCase: SaveUserNameUseCase,
    val deleteUserNameUseCase: DeleteUserNameUseCase
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
            is LoginAction.Action.OnBackPressClick -> {

            }

            is LoginAction.Action.OnLoginButtonClick -> {

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

    private fun getCurrentUiModel() = checkNotNull(_state.value?.uiModel)

}
