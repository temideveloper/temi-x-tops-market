package com.robosolutions.temixtopsmarket.ui.video

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import kotlinx.coroutines.flow.map

class PromotionFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository
) : ViewModel() {
    val qrCode = repository.qrCodeUrls
        .map { it.promotion }
        .asLiveData()
}