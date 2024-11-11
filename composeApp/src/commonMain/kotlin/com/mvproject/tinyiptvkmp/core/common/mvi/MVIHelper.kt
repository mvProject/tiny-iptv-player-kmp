package com.mvproject.tinyiptvkmp.core.common.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Stable
@Composable
fun <UiState, UiAction, UiEffect> MviCore<UiState, UiAction, UiEffect>.unpack() =
    Triple(uiState.collectAsState().value, ::onAction, uiEffect)

@Composable
fun <UiEffect> CollectUiEffect(
    effect: Flow<UiEffect>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediate,
    onUiEffect: suspend CoroutineScope.(effect: UiEffect) -> Unit,
) {
    LaunchedEffect(effect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                effect.collect { onUiEffect(it) }
            } else {
                withContext(context) {
                    effect.collect { onUiEffect(it) }
                }
            }
        }
    }
}