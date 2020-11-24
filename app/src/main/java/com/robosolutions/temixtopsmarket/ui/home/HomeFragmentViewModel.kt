package com.robosolutions.temixtopsmarket.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.DelayViewModel
import kotlinx.coroutines.flow.map

class HomeFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : DelayViewModel() {
    val autoReturnDelay = repository.delays.map { it.autoReturn }
//    val returnLocation = repository.general.map { it.autoReturnLocation }
}