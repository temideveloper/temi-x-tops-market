package com.robosolutions.temixtopsmarket.ui.location

import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentMapBinding
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BindingViewModelFragment<FragmentMapBinding, MapFragmentViewModel>() {

    override val viewModel by viewModels<MapFragmentViewModel>()

    override val layoutId = R.layout.fragment_map

    override val titleIdEn = R.string.title_store_navigation_en
    override val titleIdThai = R.string.title_store_navigation_th
}