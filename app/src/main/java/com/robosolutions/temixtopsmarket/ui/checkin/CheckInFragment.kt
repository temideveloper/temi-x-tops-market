package com.robosolutions.temixtopsmarket.ui.checkin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentCheckInBinding
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.timer
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CheckInFragment :
    BindingViewModelFragment<FragmentCheckInBinding, CheckInFragmentViewModel>() {

    override val viewModel by viewModels<CheckInFragmentViewModel>()

    override val layoutId = R.layout.fragment_check_in

    override val titleIdEn = R.string.empty
    override val titleIdThai = R.string.empty

    override val entranceSpeechId = R.string.tts_check_in

    override val headerImage = R.drawable.ic_thai_chana

    override val showHomeButton = false
    override val showSendBackButton = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val delayToReturn = viewModel.delayToReturn.singleLatest()

            timer(delayToReturn / 1000,
                onElapse = viewModel::updateCountdown,
                onTimesUp = { requireActivity().onBackPressed() }
            ).collect()
        }
    }

}