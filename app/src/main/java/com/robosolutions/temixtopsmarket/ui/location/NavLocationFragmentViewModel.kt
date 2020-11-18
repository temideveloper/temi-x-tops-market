package com.robosolutions.temixtopsmarket.ui.location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.LocationViewModel

class NavLocationFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository
) : LocationViewModel() {
    val savedLocations = repository.locations.asLiveData()

    fun saveZoneOne(location: String) = viewModelLaunch { repository.saveZoneOne(location) }

    fun saveZoneTwo(location: String) = viewModelLaunch { repository.saveZoneTwo(location) }

    fun saveZoneThree(location: String) = viewModelLaunch { repository.saveZoneThree(location) }

    fun saveZoneFour(location: String) = viewModelLaunch { repository.saveZoneFour(location) }
}