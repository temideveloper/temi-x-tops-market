package com.robosolutions.temixtopsmarket.ui.location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import kotlinx.coroutines.flow.map

class ReturningFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : ViewModel() {
    val returnLocation = repository.general
        .map { it.autoReturnLocation }
}