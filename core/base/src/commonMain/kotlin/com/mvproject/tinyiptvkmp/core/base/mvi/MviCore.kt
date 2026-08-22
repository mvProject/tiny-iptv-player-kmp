package com.mvproject.tinyiptvkmp.core.base.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun <UiState, UiAction, UiEffect> mviCore(
    initialUiState: UiState,
): MviCore<UiState, UiAction, UiEffect> = MviCoreDelegate(initialUiState)

class MviCoreDelegate<UiState, UiAction, UiEffect> internal constructor(
    initialState: UiState,
) : MviCore<UiState, UiAction, UiEffect> {

    private val _uiState = MutableStateFlow(initialState)
    override val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect by lazy { Channel<UiEffect>() }
    override val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    override fun onAction(uiAction: UiAction) {}

    override fun updateUiState(newUiState: UiState) {
        _uiState.update { newUiState }
    }

    override fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    override fun CoroutineScope.postUiEffect(effect: UiEffect) {
        this.launch { _uiEffect.send(effect) }
    }
}

interface MviCore<UiState, UiAction, UiEffect> {
    val uiState: StateFlow<UiState>
    val uiEffect: Flow<UiEffect>

    fun onAction(uiAction: UiAction)

    fun updateUiState(block: UiState.() -> UiState)

    fun updateUiState(newUiState: UiState)

    fun CoroutineScope.postUiEffect(effect: UiEffect)
}
