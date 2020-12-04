package com.robosolutions.temixtopsmarket.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber
import javax.inject.Inject

/**
 * Wrapper class for [SimpleExoPlayer] to make it a lifecycle-aware component. To register it in a [Fragment],
 * call [viewLifecycleOwner.lifecycle.addObserver(player)].
 *
 * **Note that the method must be called at least in [Fragment.onCreateView].**
 *
 * @property player The video player.
 */
class LifecycleExoPlayer @Inject constructor(val player: SimpleExoPlayer) : LifecycleObserver,
    ExoPlayer by player {

    private var wasPlaying = true

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun startPlayer() {
        Timber.d("Starting player")
        play()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer() {
        wasPlaying = player.isPlaying
        pause()

        Timber.d("Player is paused")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumePlayer() {
        if (wasPlaying) {
            play()
        }

        Timber.d(if (wasPlaying) "Player is resumed" else "Player is resumed, but no video was played")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun releasePlayer() = player.release().also { Timber.d("Player has been released") }
}