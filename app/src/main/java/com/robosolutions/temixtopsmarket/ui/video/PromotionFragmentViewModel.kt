package com.robosolutions.temixtopsmarket.ui.video

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.database.VideoRepository
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class PromotionFragmentViewModel @ViewModelInject constructor(
    repository: PreferenceRepository,
    videoRepository: VideoRepository,
    @ApplicationContext context: Context
) : ViewModel() {
    val qrCode = repository.qrCodeUrls
        .map { it.promotion }
        .asLiveData()

    val video = videoRepository.getVideos()
        .map { it.firstOrNull() }
        .filter { it != null }
        .map { it!!.getVideoFile(context) }
        .asLiveData()
}