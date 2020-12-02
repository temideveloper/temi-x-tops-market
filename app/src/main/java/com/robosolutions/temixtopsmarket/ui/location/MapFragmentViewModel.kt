package com.robosolutions.temixtopsmarket.ui.location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.LocationViewModel

class MapFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : LocationViewModel() {
    val zones = repository.locations.asLiveData()
}