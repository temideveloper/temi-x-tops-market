package com.robosolutions.temixtopsmarket.extensions

import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.File

/**
 * Plays the given video file. This adapter will only play at most one file.
 *
 * @param player The [SimpleExoPlayer] instance.
 * @param video The video file to play.
 */
@BindingAdapter("videoPlayer", "videoFile")
fun StyledPlayerView.playSource(player: SimpleExoPlayer, video: File?) {
    video ?: return

    this.player = player

    player.clearMediaItems()
    player.stop()

    player.addMediaItem(MediaItem.fromUri(video.toUri()))
    player.prepare()
    player.play()
}