package com.robosolutions.temixtopsmarket.ui.base

import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.updateTo
import kotlinx.coroutines.flow.MutableStateFlow

open class DelayViewModel : AppViewModel() {
    private val _currentCountdown = MutableStateFlow(-1)
    val currentCountdown = _currentCountdown.asLiveData()

    fun updateCountdown(count: Int) = _currentCountdown updateTo count
}