package com.robosolutions.temixtopsmarket.ui.checkin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class CheckInFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : ViewModel() {
    val qrCode = repository.qrCodeUrls
        .map { it.thaiChana }
        .asLiveData()

    val delayToReturn = repository.delays
        .map { it.checkInReturn }

    private val _currentCountdown = MutableStateFlow(-1)
    val currentCountdown = _currentCountdown.asLiveData()

    fun updateCountdown(count: Int) = _currentCountdown updateTo count
}