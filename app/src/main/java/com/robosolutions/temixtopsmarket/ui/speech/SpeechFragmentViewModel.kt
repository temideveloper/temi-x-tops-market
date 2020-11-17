package com.robosolutions.temixtopsmarket.ui.speech

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel

class SpeechFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository
) : AppViewModel() {
    val speech = repository.speech.asLiveData()

    fun saveGreeting(message: String) = viewModelLaunch { repository.saveGreeting(message) }

    fun saveExcuseMe(message: String) = viewModelLaunch { repository.saveExcuseMe(message) }
}