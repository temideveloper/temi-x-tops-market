package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentArrivedBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.timer
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ArrivedFragment :
    BindingViewModelFragment<FragmentArrivedBinding, ArrivedFragmentViewModel>() {
    override val layoutId = R.layout.fragment_arrived

    override val viewModel by viewModels<ArrivedFragmentViewModel>()

    override val useHeader = false

    val args by navArgs<ArrivedFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val autoReturnDelay = viewModel.returnDelay.singleLatest()

            timer(
                autoReturnDelay / 1000,
                onElapse = viewModel::updateCountdown,
                onTimesUp = { onSendRobotBack(view) }
            ).collect()
        }
    }

    fun onSendRobotBack(v: View) {
        v.navigate(R.id.action_arrivedFragment_to_returningFragment)
    }

    fun onViewOtherTask(v: View) {
        v.findNavController().popBackStack(R.id.homeFragment, false)
    }
}