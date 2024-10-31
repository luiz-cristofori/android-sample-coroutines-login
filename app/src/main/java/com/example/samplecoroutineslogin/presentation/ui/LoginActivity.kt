@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.samplecoroutineslogin.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplecoroutineslogin.R
import com.example.samplecoroutineslogin.helpers.collectEffect
import com.example.samplecoroutineslogin.presentation.effect.LoginUiEffect
import com.example.samplecoroutineslogin.presentation.state.LoginState
import com.example.samplecoroutineslogin.presentation.state.LoginUiModel
import com.example.samplecoroutineslogin.presentation.theme.AppTheme
import com.example.samplecoroutineslogin.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            var snackBarColor by remember { mutableStateOf(Color.Green) }

            val errorColor = MaterialTheme.colorScheme.error
            val successColor = MaterialTheme.colorScheme.primary

            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState) { snackBarData ->
                            Snackbar(
                                snackbarData = snackBarData,
                                containerColor = snackBarColor,
                                contentColor = Color.White
                            )
                        }
                    },
                    topBar = {
                        ToolBarContent()
                    },
                    content = { innerPadding ->
                        viewModel.effect.collectEffect { currentEffect ->
                            currentEffect?.let { effect ->
                                when (effect) {
                                    is LoginUiEffect.DisplaySnackBarError -> {
                                        coroutineScope.launch {
                                            snackBarColor = errorColor
                                            snackBarHostState.showSnackbar(getString(effect.messageResource))
                                        }
                                    }

                                    is LoginUiEffect.DisplaySnackBarSuccess -> {
                                        coroutineScope.launch {
                                            snackBarColor = successColor
                                            snackBarHostState.showSnackbar(getString(R.string.login_screen_success_login_text))
                                        }
                                    }
                                }
                            }
                        }

                        val state by viewModel.state.observeAsState()

                        LoginScreen(
                            innerPadding = innerPadding,
                            state = checkNotNull(state),
                            sendAction = viewModel::sendAction
                        )
                    })
            }
        }
    }
}

@Composable
private fun ToolBarContent() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.login_screen_toolbar_text),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        LoginScreen(
            innerPadding = PaddingValues(0.dp),
            state = LoginState.Resume(uiModel = LoginUiModel())
        ) { }
    }
}
