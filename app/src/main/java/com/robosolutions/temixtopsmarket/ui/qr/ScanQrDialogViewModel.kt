package com.robosolutions.temixtopsmarket.ui.qr

import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ScanQrDialogViewModel : AppViewModel() {
    private val _detectedUrl = MutableStateFlow("")
    val detectedUrl = _detectedUrl.asLiveData()

    val urlDetected = _detectedUrl.map { it.isNotBlank() }.asLiveData()

    fun updateDetectedUrl(url: String) = _detectedUrl updateTo url
}