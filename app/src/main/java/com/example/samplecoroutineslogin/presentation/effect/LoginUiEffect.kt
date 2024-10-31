package com.example.samplecoroutineslogin.presentation.effect

sealed class LoginUiEffect {
    data class DisplaySnackBarError(val messageResource: Int) : LoginUiEffect()
    data object DisplaySnackBarSuccess : LoginUiEffect()
}
