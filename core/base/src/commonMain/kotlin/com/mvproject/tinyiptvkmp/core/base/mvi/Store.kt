package com.mvproject.tinyiptvkmp.core.base.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


interface Store<State, Intent, Effect> {
    val stateFlow: StateFlow<State>
    val effect: Flow<Effect>

    fun getState(): State
    fun setState(reducer: State.() -> State)
    fun sendEffect(effect: Effect)
}

internal class StoreImpl<State, Intent, Effect>(
    private val initialState: State,
    started: SharingStarted = SharingStarted.Eagerly,
    private val invokeOnStart: (suspend () -> Unit)? = null,
    private val parentScope: CoroutineScope,
) : Store<State, Intent, Effect> {

    private val _stateFlow = MutableStateFlow(initialState)

    /*    override val stateFlow: StateFlow<State> = _stateFlow
            .let { source ->
                val subscribersExpected = started != SharingStarted.Eagerly

                if (invokeOnStart != null && subscribersExpected) {
                    source.onStart {
                        invokeOnStart.invoke()
                    }
                } else {
                    parentScope.launch {
                        yield()
                        invokeOnStart?.invoke()
                    }
                    source
                }
            }.stateIn(
                scope = parentScope,
                started = started,
                initialValue = initialState,
            )*/

    override val stateFlow: StateFlow<State> = _stateFlow
        .let { source ->
            val subscribersExpected = started != SharingStarted.Eagerly

            if (invokeOnStart != null && subscribersExpected) {
                var invokeOnStartJob: Job? = null

                source
                    .onStart {
                        invokeOnStartJob = parentScope.launch {
                            invokeOnStart.invoke()
                        }
                    }
                    .onCompletion {
                        invokeOnStartJob?.cancel()
                        invokeOnStartJob = null
                    }
            } else {
                parentScope.launch {
                    yield()
                    invokeOnStart?.invoke()
                }
                source
            }
        }.stateIn(
            scope = parentScope,
            started = started,
            initialValue = initialState,
        )

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    override val effect: Flow<Effect> = _effect.receiveAsFlow()

    private var startedJob: Job? = null

    // Guard for single launch if multiple collectors or use in shared viewmodel
    private fun startOnce() {
        if (startedJob != null) return
        startedJob = parentScope.launch {
            invokeOnStart?.invoke()
        }
    }

    override fun getState(): State {
        return _stateFlow.value
    }

    override fun setState(reducer: State.() -> State) {
        _stateFlow.update(reducer)
    }

    override fun sendEffect(effect: Effect) {
        parentScope.launch {
            _effect.send(effect)
        }
    }
}
