package com.robosolutions.temixtopsmarket.ui.video

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentPromotionBinding
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robosolutions.temixtopsmarket.utils.LifecycleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onBinding(binding: FragmentPromotionBinding) {
        super.onBinding(binding)

        viewLifecycleOwner.lifecycle.addObserver(videoPlayer)
    }
}