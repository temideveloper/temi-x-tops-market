package com.robosolutions.temixtopsmarket.ui.location

import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentNavLocationBinding
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavLocationFragment :
    BindingViewModelFragment<FragmentNavLocationBinding, NavLocationFragmentViewModel>() {

    override val viewModel by viewModels<NavLocationFragmentViewModel>()

    override val layoutId = R.layout.fragment_nav_location

    override val titleIdEn = R.string.title_nav_location_en
    override val titleIdThai = R.string.title_nav_location_th
}