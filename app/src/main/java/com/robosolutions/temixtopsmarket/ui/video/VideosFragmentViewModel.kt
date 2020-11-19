package com.robosolutions.temixtopsmarket.ui.video

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.database.PromotionVideo
import com.robosolutions.temixtopsmarket.database.VideoRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

class VideosFragmentViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: VideoRepository
) : AppViewModel() {
    val videos = repository.getVideos().asLiveData()

    fun removeVideo(video: PromotionVideo) {
        // Delete the video
        video.getVideoFile(context).run {
            if (exists()) {
                delete().also { deleted ->
                    if (deleted) Timber.d("Deleted video file for ${video.id}")
                    else Timber.w("Video not deleted for file ${video.id}!")
                }
            }
        }

        // Remove video info from database
        viewModelLaunch { repository.deleteVideo(video) }
    }
}