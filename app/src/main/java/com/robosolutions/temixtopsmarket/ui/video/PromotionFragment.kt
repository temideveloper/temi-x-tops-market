package com.robosolutions.temixtopsmarket.ui.video

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentPromotionBinding
import com.robosolutions.temixtopsmarket.extensions.timer
import com.robosolutions.temixtopsmarket.ui.activity.MainActivity
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robosolutions.temixtopsmarket.utils.LifecycleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PromotionFragment :
    BindingViewModelFragment<FragmentPromotionBinding, PromotionFragmentViewModel>() {
    override val viewModel by viewModels<PromotionFragmentViewModel>()

    override val layoutId = R.layout.fragment_promotion

    override val titleIdEn = R.string.title_promotions_en
    override val titleIdThai = R.string.title_promotions_th

    override val entranceSpeechId = R.string.tts_promotion

    override val showHomeButton = false
    override val showSendBackButton = true

    @Inject
    lateinit var videoPlayer: LifecycleExoPlayer

    private lateinit var displayDialogJob: Job

    fun cancelDialogJob() {
        if (::displayDialogJob.isInitialized) {
            displayDialogJob.cancel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        videoPlayer.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                Timber.d("Is video playing? $isPlaying")
                if (isPlaying) {
                    cancelDialogJob()
                } else {
                    displayDialogJob = lifecycleScope.launch {
                        timer(15,
                            onElapse = {
                                if (it % 5 == 0) {
                                    Timber.d("$it seconds before displaying dialog")
                                }
                            },
                            onTimesUp = {
                                (requireActivity() as MainActivity).startAutoReturnDialog(
                                    onCancel = this@PromotionFragment::resumePlayback,
                                    onWait = this@PromotionFragment::resumePlayback
                                )
                            }
                        ).collect()
                    }
                }
            }
        })
    }

    fun resumePlayback() {
        if (videoPlayer.player.playbackState == SimpleExoPlayer.STATE_ENDED) {
            videoPlayer.player.seekTo(0)
        }

        videoPlayer.play()
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.switchUserInteractionDetection(false)
    }

    override fun onPause() {
        super.onPause()

        cancelDialogJob()
        mainViewModel.switchUserInteractionDetection(true)
    }

    override fun onBinding(binding: FragmentPromotionBinding) {
        super.onBinding(binding)

        viewLifecycleOwner.lifecycle.addObserver(videoPlayer)
    }
}