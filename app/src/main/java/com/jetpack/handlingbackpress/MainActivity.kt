package com.jetpack.handlingbackpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import com.jetpack.handlingbackpress.ui.theme.HandlingBackPressTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandlingBackPressTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Handling BackPress",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            HandlingBackPress()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HandlingBackPress() {
    var viewState by remember { mutableStateOf(0) }

    when (viewState) {
        1 -> BackPressHandle(enabled = true) {
            viewState = 0
        }
        2 -> BackPressHandle(enabled = true) {
            viewState = 1
        }
        3 -> BackPressHandle(enabled = true) {
            viewState = 3
        }
    }

    Column {
        when (viewState) {
            0 -> {
                Button(
                    onClick = { viewState = 1 }
                ) {
                    Text(text = "OnBackPress 1")
                }
            }
            1 -> {
                Button(
                    onClick = { viewState = 2 }
                ) {
                    Text(text = "OnBackPress 2")
                }
            }
            2 -> {
                Button(
                    onClick = { viewState = 3 }
                ) {
                    Text(text = "OnBackPress 3")
                }
            }
            3 -> {
                Button(
                    onClick = { viewState = 0 }
                ) {
                    Text(text = "OnBackPress 0")
                }
            }
        }
    }
}

@Composable
fun BackPressHandle(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    val currentOnBack by rememberUpdatedState(newValue = onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifeCycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifeCycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}






















