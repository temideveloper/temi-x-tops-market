package com.robosolutions.temixtopsmarket.ui.video

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentVideoPlayBinding
import com.robosolutions.temixtopsmarket.extensions.themeColor
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robosolutions.temixtopsmarket.utils.LifecycleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoPlayFragment :
    BindingViewModelFragment<FragmentVideoPlayBinding, VideoPlayFragmentViewModel>() {

    override val viewModel by viewModels<VideoPlayFragmentViewModel>()

    private val args by navArgs<VideoPlayFragmentArgs>()

    override val layoutId = R.layout.fragment_video_play

    @Inject
    lateinit var player: LifecycleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }

        viewModel.playVideo(args.videoId)
    }

    override fun onBinding(binding: FragmentVideoPlayBinding) {
        super.onBinding(binding)

        viewLifecycleOwner.lifecycle.addObserver(player)

        viewModel.videoToPlayDetails.observe(viewLifecycleOwner) { (title, summary) ->
            updateThaiTitle(title)
            updateEnglishTitle(summary)
        }
    }
}