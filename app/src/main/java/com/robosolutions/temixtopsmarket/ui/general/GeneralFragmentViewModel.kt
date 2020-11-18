package com.robosolutions.temixtopsmarket.ui.general

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel

class GeneralFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository
) : AppViewModel() {
    val general = repository.general.asLiveData()

    val locations = robot.locations

    fun saveDetectionRange(range: Float) = viewModelLaunch { repository.saveDetectionRange(range) }

    fun saveAutoReturnLocation(location: String) =
        viewModelLaunch { repository.saveAutoReturnLocation(location) }
}