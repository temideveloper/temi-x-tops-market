package com.robosolutions.temixtopsmarket.ui.video

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.adapter.PromotionVideoAdapter
import com.robosolutions.temixtopsmarket.databinding.FragmentVideosBinding
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideosFragment : BindingViewModelFragment<FragmentVideosBinding, VideosFragmentViewModel>() {
    override val viewModel by viewModels<VideosFragmentViewModel>()

    override val layoutId = R.layout.fragment_videos

    override val titleIdEn = R.string.title_promotions_en
    override val titleIdThai = R.string.title_promotions_th

    private val args by navArgs<VideosFragmentArgs>()
    val isEditMode by lazy { args.isEditMode }

    val adapter by lazy { PromotionVideoAdapter(isEditMode) }

    val layoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.videos.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}