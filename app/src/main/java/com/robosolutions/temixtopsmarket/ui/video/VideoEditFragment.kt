package com.robosolutions.temixtopsmarket.ui.video

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.google.android.material.transition.MaterialContainerTransform
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentVideoEditBinding
import com.robosolutions.temixtopsmarket.extensions.textString
import com.robosolutions.temixtopsmarket.extensions.themeColor
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_video_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import timber.log.Timber

@AndroidEntryPoint
class VideoEditFragment :
    BindingViewModelFragment<FragmentVideoEditBinding, VideoEditFragmentViewModel>() {
    override val viewModel by viewModels<VideoEditFragmentViewModel>()

    private val args by navArgs<VideoEditFragmentArgs>()

    private val fetchVideo = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) return@registerForActivityResult

        val inputStream = lifecycleScope.async(Dispatchers.IO) {
            requireContext().contentResolver.openInputStream(it)
        }

        viewModel.setPreviewVideo(inputStream)
    }

    override val useHeader = false

    override val contentPadding: Int? = null

    override val layoutId = R.layout.fragment_video_edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.videoId.isNotBlank()) {
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                drawingViewId = R.id.navHostFragment
                scrimColor = Color.TRANSPARENT
                setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setVideoId(args.videoId)

        if (args.videoId == "") {
            enterTransition = MaterialContainerTransform().apply {

                startView = requireActivity().findViewById(R.id.fabAddVideo)
                endView = videoCardView

                scrimColor = Color.TRANSPARENT
                containerColor = requireContext().themeColor(R.attr.colorSurface)
                startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
                endContainerColor = requireContext().themeColor(R.attr.colorSurface)
            }

            returnTransition = Slide().apply {
                addTarget(videoCardView)
            }
        }
    }

    fun onCancel() = requireActivity().onBackPressed()

    fun onSaveChanges() = lifecycleScope.launchWhenCreated {
        viewModel.saveVideoDetails(
            id = args.videoId,
            title = editTextVideoTitle.textString,
            summary = editTextVideoSummary.textString
        )
        Timber.d("Saved video details")

        requireActivity().onBackPressed()
    }

    fun onChangeVideo() {
        fetchVideo.launch("video/*")
    }
}