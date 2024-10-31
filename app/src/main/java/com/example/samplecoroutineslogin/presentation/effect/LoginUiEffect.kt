package com.example.samplecoroutineslogin.presentation.effect

sealed class LoginUiEffect {
    data object BackPress : LoginUiEffect()
    data object SubmitLogin : LoginUiEffect()
}
