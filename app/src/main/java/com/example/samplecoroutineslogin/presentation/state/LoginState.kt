package com.example.samplecoroutineslogin.presentation.state

sealed class LoginState(open val uiModel: LoginUiModel = LoginUiModel()) {
    data object Loading : LoginState()
    data class Resume(override val uiModel: LoginUiModel) : LoginState(uiModel)
    data object Error : LoginState()
}

data class LoginUiModel(
    val isRequestInProgress: Boolean = false,
    val isRememberLoginChecked: Boolean = false,
    val userNameInputText: String = "",
    val passwordInputText: String = ""
)
