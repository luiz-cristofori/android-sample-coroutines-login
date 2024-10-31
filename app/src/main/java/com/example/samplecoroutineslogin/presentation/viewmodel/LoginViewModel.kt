package com.example.samplecoroutineslogin.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samplecoroutineslogin.presentation.action.LoginAction
import com.example.samplecoroutineslogin.presentation.effect.LoginUiEffect
import com.example.samplecoroutineslogin.presentation.state.LoginState

class LoginViewModel : ViewModel() {

    private val _state = MutableLiveData<LoginState>(LoginState.Loading)
    val state = _state.asLiveData()

    private val _effect = MutableSingleLiveEvent<LoginUiEffect>()
    val effect = _effect.asSingleLiveEvent()

    init {
        // MOCK
        _state.value =
            LoginState.Resume(
                uiModel = getCurrentUiModel()
            )
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
                _state.value =
                    LoginState.Resume(
                        uiModel = getCurrentUiModel().copy(
                            isRememberLoginChecked = action.isChecked
                        )
                    )
            }

            is LoginAction.Action.OnUserNameInputChange -> {
                _state.value =
                    LoginState.Resume(
                        uiModel = getCurrentUiModel().copy(
                            userNameInputText = action.text
                        )
                    )
            }
        }
    }

    private fun getCurrentUiModel() = checkNotNull(_state.value?.uiModel)

}
