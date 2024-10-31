@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.samplecoroutineslogin.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.samplecoroutineslogin.R
import com.example.samplecoroutineslogin.presentation.effect.LoginUiEffect
import com.example.samplecoroutineslogin.presentation.theme.MainTheme
import com.example.samplecoroutineslogin.presentation.viewmodel.LoginViewModel
import com.example.samplecoroutineslogin.presentation.viewmodel.collectEffect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        ToolBarContent()
                    },
                    content = { innerPadding ->
                        viewModel.effect.collectEffect { currentEffect ->
                            currentEffect?.let { effect ->
                                when (effect) {
                                    LoginUiEffect.BackPress -> {

                                    }

                                    LoginUiEffect.SubmitLogin -> {

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainTheme {

    }
}
