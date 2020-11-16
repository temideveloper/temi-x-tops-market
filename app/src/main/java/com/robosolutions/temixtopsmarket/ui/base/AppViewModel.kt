package com.robosolutions.temixtopsmarket.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

open class AppViewModel : ViewModel() {
    /**
     * Sends value to the [MutableSharedFlow] asynchronously. This function will create a new child
     * coroutine.
     *
     * @param value The value to send to the flow.
     */
    protected infix fun <T> MutableSharedFlow<T>.launchAndEmit(value: T) = viewModelScope.launch {
        this@launchAndEmit.emit(value)
    }
}