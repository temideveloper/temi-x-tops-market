package com.robosolutions.temixtopsmarket.ui.base

import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * [AppViewModel] that contains temi robot's saved locations. When there is a change to the robot's
 * saved locations. The list will be automatically updated.
 *
 */
open class LocationViewModel : AppViewModel(), OnLocationsUpdatedListener {
    private val _locations = MutableStateFlow(robot.locations)
        .also { robot.addOnLocationsUpdatedListener(this) }
    val locations = _locations.asLiveData()

    override fun onCleared() {
        super.onCleared()

        robot.removeOnLocationsUpdateListener(this)
    }

    override fun onLocationsUpdated(locations: List<String>) {
        _locations updateTo locations
    }
}