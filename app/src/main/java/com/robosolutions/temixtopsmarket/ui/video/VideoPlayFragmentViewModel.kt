package com.robosolutions.temixtopsmarket.ui.video

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.database.VideoRepository
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import java.util.*

@OptIn(FlowPreview::class)
class VideoPlayFragmentViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: VideoRepository
) : AppViewModel() {
    private val videoId = MutableStateFlow("")

    private val targetVideo = videoId.filter { it.isNotBlank() }
        .flatMapConcat { repository.getVideoById(UUID.fromString(it)) }

    /** The video file to play. */
    val videoToPlay = targetVideo.map { it.getVideoFile(context) }
        .asLiveData()

    /** The details are title and summary respectively. */
    val videoToPlayDetails = targetVideo.map { it.title to it.summary }.asLiveData()

    fun playVideo(id: String) = videoId updateTo id
}