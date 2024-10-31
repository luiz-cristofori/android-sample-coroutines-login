package com.example.samplecoroutineslogin.presentation.action

interface LoginAction {
    fun sendAction(action: Action)
    sealed class Action {
        data object OnTryAgainClick : Action()
        data class OnLoginButtonClick(val userName: String, val password: String) : Action()
        data class OnUserNameInputChange(val text: String) : Action()
        data class OnPasswordInputChange(val text: String) : Action()
        data class OnRememberUserNameChecked(val isChecked: Boolean) : Action()
    }
}
