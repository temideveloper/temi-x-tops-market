package com.robosolutions.temixtopsmarket.ui.delays

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel

class DelaysFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository
) : AppViewModel() {
    val delays = repository.delays.asLiveData()

    fun saveAutoReturnDelay(delayMs: Int) =
        viewModelLaunch { repository.saveAutoReturnDelay(delayMs) }

    fun saveCheckInReturnDelay(delayMs: Int) =
        viewModelLaunch { repository.saveCheckInReturnDelay(delayMs) }

    fun saveExcuseMeInterval(delayMs: Int) =
        viewModelLaunch { repository.saveExcuseMeInterval(delayMs) }
}