package com.robosolutions.temixtopsmarket.ui.location

import androidx.hilt.lifecycle.ViewModelInject
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.DelayViewModel
import kotlinx.coroutines.flow.map

class ArrivedFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : DelayViewModel() {
    val returnDelay = repository.delays
        .map { it.autoReturn }
}