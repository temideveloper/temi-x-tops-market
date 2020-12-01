package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentArrivedBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.timer
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArrivedFragment :
    BindingViewModelFragment<FragmentArrivedBinding, ArrivedFragmentViewModel>() {
    override val layoutId = R.layout.fragment_arrived

    override val viewModel by viewModels<ArrivedFragmentViewModel>()

    override val useHeader = false

    val args by navArgs<ArrivedFragmentArgs>()

    override val entranceSpeechId = R.string.tts_arrived
    override val entranceSpeechArgs: Array<Any?>? by lazy { arrayOf(args.location) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        mainViewModel.mapRevisited = true
    }

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

    fun onSendRobotBack(v: View) = lifecycleScope.launch {
        exitTransition = MaterialFadeThrough()

        val returnLocation = mainViewModel.autoReturnLocation.singleLatest()
        val dir = ArrivedFragmentDirections.actionArrivedFragmentToReturningFragment(returnLocation)

        v.navigate(dir)
    }

    fun onViewOtherTask(v: View) {
        // To home screen
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        v.findNavController().popBackStack(R.id.homeFragment, false)
    }
}