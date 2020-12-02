package com.robosolutions.temixtopsmarket.ui.location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.LocationViewModel
import kotlinx.coroutines.flow.take

class NavLocationFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository
) : LocationViewModel() {
    val savedLocations = repository.locations.asLiveData()
    val lastSavedLocations = repository.locations.take(1).asLiveData()

    fun saveZoneOne(location: String) = viewModelLaunch { repository.saveZoneOne(location) }

    fun saveZoneTwo(location: String) = viewModelLaunch { repository.saveZoneTwo(location) }

    fun saveZoneThree(location: String) = viewModelLaunch { repository.saveZoneThree(location) }

    fun saveZoneFour(location: String) = viewModelLaunch { repository.saveZoneFour(location) }

    fun saveZoneFive(location: String) = viewModelLaunch { repository.saveZoneFive(location) }

    fun saveZoneSix(location: String) = viewModelLaunch { repository.saveZoneSix(location) }

    fun saveZoneSeven(location: String) = viewModelLaunch { repository.saveZoneSeven(location) }

    fun saveZoneEight(location: String) = viewModelLaunch { repository.saveZoneEight(location) }

    fun saveZoneNine(location: String) = viewModelLaunch { repository.saveZoneNine(location) }

    fun saveZoneTen(location: String) = viewModelLaunch { repository.saveZoneTen(location) }

    fun saveZoneEleven(location: String) = viewModelLaunch { repository.saveZoneEleven(location) }

    fun saveZoneTwelve(location: String) = viewModelLaunch { repository.saveZoneTwelve(location) }

    fun saveZoneThirteen(location: String) =
        viewModelLaunch { repository.saveZoneThirteen(location) }

    fun saveZoneFourteen(location: String) =
        viewModelLaunch { repository.saveZoneFourteen(location) }

    fun saveZoneFifteen(location: String) = viewModelLaunch { repository.saveZoneFifteen(location) }

    fun saveZoneSixteen(location: String) = viewModelLaunch { repository.saveZoneSixteen(location) }

    fun saveZoneSeventeen(location: String) =
        viewModelLaunch { repository.saveZoneSeventeen(location) }
}