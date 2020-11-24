package com.robosolutions.temixtopsmarket.ui.checkin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.DelayViewModel
import kotlinx.coroutines.flow.map

class CheckInFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : DelayViewModel() {
    val qrCode = repository.qrCodeUrls
        .map { it.thaiChana }
        .asLiveData()

    val delayToReturn = repository.delays
        .map { it.checkInReturn }
}