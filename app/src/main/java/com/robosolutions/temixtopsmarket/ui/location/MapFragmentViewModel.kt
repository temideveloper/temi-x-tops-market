package com.robosolutions.temixtopsmarket.ui.location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository

class MapFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : ViewModel() {
    val zones = repository.locations.asLiveData()
}