package com.robosolutions.temixtopsmarket.ui.video

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.robosolutions.temixtopsmarket.database.PromotionVideo
import com.robosolutions.temixtopsmarket.database.VideoRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel

class VideosFragmentViewModel @ViewModelInject constructor(
    private val repository: VideoRepository
) : AppViewModel() {
//        val videos = repository.getVideos().asLiveData()

//    val videos =
//        MutableLiveData((1..10).map { PromotionVideo("Video $it", "The summary for video $it") })
}