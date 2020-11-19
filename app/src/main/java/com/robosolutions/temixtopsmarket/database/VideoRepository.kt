package com.robosolutions.temixtopsmarket.database

import android.content.Context
import androidx.annotation.WorkerThread
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class VideoRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val dao: PromotionVideoDao
) : PromotionVideoDao by dao {

    /** Parent directory for videos. */
    private val parentDirectory = File(context.applicationContext.filesDir, VIDEO_FOLDER).also {
        it.mkdirs()
    }

    @WorkerThread
    fun saveVideoFile(title: String, input: InputStream) =
        FileOutputStream(getVideoFile(title), false).use {
            input.copyTo(it)

            Timber.d("Saved video for title $title")
        }

    fun getVideoFile(title: String) = File(parentDirectory, title)

    companion object {
        const val VIDEO_FOLDER = "videos"
    }
}