package com.mvproject.tinyiptvkmp.core.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptvkmp.infrastructure.logging.injectLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class MviViewModel<State, Intent, Effect> : ViewModel(), KoinComponent {

    protected val logger by injectLogger()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    protected abstract fun createStore(): Store<State, Intent, Effect>

    protected val store: Store<State, Intent, Effect> by lazy {
        createStore()
    }

    val state: StateFlow<State>
        get() = store.stateFlow

    val effect: Flow<Effect>
        get() = store.effect

    abstract fun onIntent(intent: Intent)

    protected fun createStore(
        initialState: State,
        started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
        invokeOnStart: (suspend () -> Unit)? = null,
    ): Store<State, Intent, Effect> {
        return StoreImpl(
            initialState = initialState,
            started = started,
            invokeOnStart = invokeOnStart,
            parentScope = viewModelScope,
        )

    }

    protected fun getState(): State {
        return store.getState()
    }

    protected fun setState(reducer: State.() -> State) {
        store.setState(reducer)
    }

    protected fun sendEffect(effect: Effect) {
        store.sendEffect(effect)
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            block()
        }
    }

    protected open fun handleError(t: Throwable) {
        logger.e(t) { "Unhandled error" }
    }
}