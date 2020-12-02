package com.robosolutions.temixtopsmarket.ui.location

import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class NavigatingFragmentViewModel : AppViewModel() {
    private val _latestGoToDesc = MutableSharedFlow<String>()
    val latestGoToDesc: SharedFlow<String> = _latestGoToDesc

    fun updateGoToDescription(desc: String) = _latestGoToDesc launchAndEmit desc
}