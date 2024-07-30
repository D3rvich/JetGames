package ru.d3rvich.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Created by Ilya Deryabin at 02.02.2024
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent, Action : UiAction> : ViewModel() {

    private val initialState: State by lazy { createInitialState() }
    protected abstract fun createInitialState(): State

    val currentState: State
        get() = uiState.value

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiAction = Channel<Action>()
    val uiAction = _uiAction.receiveAsFlow()

    protected fun setState(state: State) {
        _uiState.value = state
    }

    protected fun sendAction(block: () -> Action) {
        viewModelScope.launch {
            val action = block()
            _uiAction.send(action)
        }
    }

    abstract fun obtainEvent(event: Event)

    protected fun unexpectedEventError(event: Event, state: State = currentState): Nothing =
        error("Unexpected ${event.javaClass.simpleName} event for ${state.javaClass.simpleName} state")
}