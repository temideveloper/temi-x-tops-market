package com.robosolutions.temixtopsmarket.ui.video

import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentPromotionBinding
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromotionFragment :
    BindingViewModelFragment<FragmentPromotionBinding, PromotionFragmentViewModel>() {
    override val viewModel by viewModels<PromotionFragmentViewModel>()

    override val layoutId = R.layout.fragment_promotion

    override val titleIdEn = R.string.title_promotions_en
    override val titleIdThai = R.string.title_promotions_th
}