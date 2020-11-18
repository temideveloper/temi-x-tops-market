package com.robosolutions.temixtopsmarket.ui.general

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import com.robosolutions.temixtopsmarket.ui.base.LocationViewModel

class GeneralFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository
) : LocationViewModel() {
    val general = repository.general.asLiveData()

    fun saveDetectionRange(range: Float) = viewModelLaunch { repository.saveDetectionRange(range) }

    fun saveAutoReturnLocation(location: String) =
        viewModelLaunch { repository.saveAutoReturnLocation(location) }
}