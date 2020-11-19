package com.robosolutions.temixtopsmarket.ui.video

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.adapter.PromotionVideoAdapter
import com.robosolutions.temixtopsmarket.databinding.FragmentVideosBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_videos.*

@AndroidEntryPoint
class VideosFragment : BindingViewModelFragment<FragmentVideosBinding, VideosFragmentViewModel>() {
    override val viewModel by viewModels<VideosFragmentViewModel>()

    override val layoutId = R.layout.fragment_videos

    override val titleIdEn = R.string.title_promotions_en
    override val titleIdThai = R.string.title_promotions_th

    private val args by navArgs<VideosFragmentArgs>()
    val isEditMode by lazy { args.isEditMode }

    val adapter by lazy {
        PromotionVideoAdapter(isEditMode, requireContext(),

            onClickCard = { v, video ->
                showVideoDetail(v, video.id.toString())
            },

            onClickRemove = {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.dialog_remove_title)
                    .setMessage(R.string.dialog_remove_content)
                    .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.removeVideo(it) }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Wait until recycler view is calculated before starting transition
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.videos.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    fun showVideoDetail(v: View, videoId: String) {
        val dir =
            VideosFragmentDirections.actionVideosFragmentToVideoEditFragment(videoId)

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)

        val detailTransitionName = getString(R.string.transition_name_video_details)
        val extras = FragmentNavigatorExtras(v to detailTransitionName)

        if (videoId.isNotBlank()) {
            // For existing video
            v.navigate(dir, extras)
        } else {
            // For new video
            v.navigate(dir)
        }
    }
}