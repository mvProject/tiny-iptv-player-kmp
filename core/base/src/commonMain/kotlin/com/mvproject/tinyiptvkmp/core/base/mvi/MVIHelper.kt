package com.mvproject.tinyiptvkmp.core.base.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    Triple(uiState.collectAsStateWithLifecycle().value, ::onAction, uiEffect)

@Composable
fun <UiEffect> CollectUiEffect(
    effect: Flow<UiEffect>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = Dispatchers.Main.immediate,
    onUiEffect: suspend CoroutineScope.(effect: UiEffect) -> Unit,
) {
    val currentOnUiEffect by rememberUpdatedState(onUiEffect)
    LaunchedEffect(effect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                effect.collect { currentOnUiEffect(it) }
            } else {
                withContext(context) {
                    effect.collect { currentOnUiEffect(it) }
                }
            }
        }
    }
}
