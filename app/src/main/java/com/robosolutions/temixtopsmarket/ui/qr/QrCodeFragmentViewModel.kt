package com.robosolutions.temixtopsmarket.ui.qr

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel

class QrCodeFragmentViewModel @ViewModelInject constructor(
    private val repository: PreferenceRepository,
) : AppViewModel() {
    val qrCodeUrls = repository.qrCodeUrls.asLiveData()

    fun saveThaiChanaUrl(url: String) = viewModelLaunch { repository.saveThaiChanaUrl(url) }

    fun savePromotionUrl(url: String) = viewModelLaunch { repository.savePromotionUrl(url) }
}