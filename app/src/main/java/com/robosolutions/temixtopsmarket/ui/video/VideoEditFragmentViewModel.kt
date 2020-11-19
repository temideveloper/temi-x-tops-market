package com.robosolutions.temixtopsmarket.ui.video

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.robosolutions.temixtopsmarket.database.PromotionVideo
import com.robosolutions.temixtopsmarket.database.VideoRepository
import com.robosolutions.temixtopsmarket.extensions.decrement
import com.robosolutions.temixtopsmarket.extensions.increment
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.util.*

@OptIn(FlowPreview::class)
class VideoEditFragmentViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: VideoRepository,
) : AppViewModel() {
    private val currentVideoId = MutableStateFlow("")

    private val _currentVideo = currentVideoId.flatMapConcat {
        if (it == "") {
            Timber.d("Configuring new video")

            flow { emit(PromotionVideo("", "")) }
        } else {
            Timber.d("Configuring existing video")

            repository.getVideoById(UUID.fromString(it))
        }
    }

    val currentVideo = _currentVideo.asLiveData()

    private val _videoFile = MutableSharedFlow<File>()
    val videoFile = _videoFile.asLiveData()

    private val taskCount = MutableStateFlow(0)
    val isProcessing = taskCount.map { it != 0 }.asLiveData()

    init {
        viewModelLaunch {
            _currentVideo.collect {
                _videoFile.emit(it.getVideoFile(context))
            }
        }
    }

    fun setVideoId(videoId: String) = currentVideoId updateTo videoId

    fun setPreviewVideo(input: Deferred<InputStream?>) {
        // Download file to tmp
        viewModelScope.launch {
            withLoading(onError = {
                Timber.e(it, "Encountered exception when downloading video")
            }) {
                val inputStream = input.await() ?: return@withLoading

                inputStream.use {
                    repository.saveVideoFile(TEMP_VIDEO_FILE, it)
                }

                _videoFile.emit(repository.getVideoFile(TEMP_VIDEO_FILE))
            }
        }

    }

    /**
     * Saves the additional info of the video to the database, and rename the temporary file
     * used to store the video.
     *
     * @param id The id of the video. Use empty string to indicate new video.
     * @param title The video title.
     * @param summary The video summary.
     */
    suspend fun saveVideoDetails(id: String, title: String, summary: String) = withLoading {
        val video = if (id.isNotBlank()) {
            PromotionVideo(title, summary, UUID.fromString(id))
        } else {
            PromotionVideo(title, summary)
        }

        // Save to database
        repository.saveVideo(video)
        Timber.d("Saving video with id ${video.id}")

        // Rename temporary file
        repository.getVideoFile(TEMP_VIDEO_FILE).run {
            if (exists()) {
                renameTo(video.getVideoFile(context)).also { renamed ->
                    if (renamed) {
                        Timber.d("Renamed tmp file to ${video.id}")
                    } else {
                        Timber.d("Tmp file not renamed")
                    }
                }
            }
        }
    }

    private suspend fun withLoading(onError: (Throwable) -> Unit = {}, block: suspend () -> Unit) =
        try {
            taskCount.increment()
            Timber.d("Task count is now ${taskCount.value}")
            block()
        } catch (e: Exception) {
            onError(e)
        } finally {
            taskCount.decrement()
            Timber.d("Task count is now ${taskCount.value}")
        }

    override fun onCleared() {
        super.onCleared()

        // Remove tmp file
        repository.getVideoFile(TEMP_VIDEO_FILE).run {
            if (exists()) {
                delete().also { deleted ->
                    if (deleted) {
                        Timber.d("Deleted temporary file $TEMP_VIDEO_FILE")
                    } else {
                        Timber.d("Failed to delete temporary file $TEMP_VIDEO_FILE")
                    }
                }
            }
        }
    }

    companion object {
        const val TEMP_VIDEO_FILE = "tmp"
    }
}