package com.example.samplecoroutineslogin.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.samplecoroutineslogin.R
import com.example.samplecoroutineslogin.presentation.action.LoginAction
import com.example.samplecoroutineslogin.presentation.state.LoginState
import com.example.samplecoroutineslogin.presentation.state.LoginUiModel

@Composable
fun LoginScreen(
    innerPadding: PaddingValues,
    state: LoginState,
    sendAction: (LoginAction.Action) -> Unit
) {
    when (state) {
        is LoginState.Loading -> {
            LoadingContent(innerPadding)
        }

        is LoginState.Resume -> {
            LoginContent(
                innerPadding = innerPadding,
                uiModel = state.uiModel,
                sendAction = sendAction
            )
        }

        is LoginState.Error -> {
            ErrorContent(innerPadding, sendAction)
        }
    }

}

@Composable
fun LoginContent(
    innerPadding: PaddingValues,
    uiModel: LoginUiModel,
    sendAction: (LoginAction.Action) -> Unit
) {
    LoadingDialog(uiModel.isRequestInProgress)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = uiModel.userNameInputText,
            onValueChange = {
                sendAction(LoginAction.Action.OnUserNameInputChange(it))
            },
            label = {
                Text(stringResource(R.string.login_screen_username_text_field_label))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.size(4.dp))
        TextField(
            value = uiModel.passwordInputText,
            onValueChange = {
                sendAction(LoginAction.Action.OnPasswordInputChange(it))
            },
            label = {
                Text(stringResource(R.string.login_screen_password_text_field_label))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Checkbox(
                checked = uiModel.isRememberLoginChecked,
                onCheckedChange = {
                    sendAction(LoginAction.Action.OnRememberUserNameChecked(!uiModel.isRememberLoginChecked))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.login_screen_remember_username_text))
        }
        Spacer(modifier = Modifier.size(4.dp))
        Button(
            modifier = Modifier.width(150.dp),
            onClick = {}
        ) {
            Text(stringResource(R.string.login_screen_submit_button_label))
        }
    }
}

@Composable
fun LoadingDialog(
    isLoading: Boolean,
) {
    if (isLoading) {
        Dialog(onDismissRequest = { }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = stringResource(R.string.login_screen_request_loading_text),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorContent(innerPadding: PaddingValues, sendAction: (LoginAction.Action) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.vector_error_outline),
            contentDescription = "",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
        )
        Text(
            text = stringResource(R.string.login_screen_error_text),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.width(150.dp),
            onClick = {
                sendAction(LoginAction.Action.OnTryAgainClick)
            }
        ) {
            Text("Try Again")
        }
    }
}

@Composable
fun LoadingContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenFilledResumePreview() {
    val uiModel = LoginUiModel(
        userNameInputText = "username",
        passwordInputText = "123",
        isRememberLoginChecked = true
    )
    LoginScreen(
        innerPadding = PaddingValues(0.dp),
        state = LoginState.Resume(uiModel),
    ) {}
}

@Preview(showBackground = true)
@Composable
fun LoginScreenResumePreview() {
    LoginScreen(
        innerPadding = PaddingValues(0.dp),
        state = LoginState.Resume(LoginUiModel()),
    ) {}
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreen(
        innerPadding = PaddingValues(0.dp),
        state = LoginState.Loading,
    ) {}
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    LoginScreen(
        innerPadding = PaddingValues(0.dp),
        state = LoginState.Error,
    ) {}
}
