package com.robosolutions.temixtopsmarket.extensions

import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.File

@BindingAdapter("videoPlayer", "videoFile")
fun StyledPlayerView.playSource(player: SimpleExoPlayer, video: File?) {
    video ?: return

    this.player = player

    player.addMediaItem(MediaItem.fromUri(video.toUri()))
    player.prepare()
    player.play()
}