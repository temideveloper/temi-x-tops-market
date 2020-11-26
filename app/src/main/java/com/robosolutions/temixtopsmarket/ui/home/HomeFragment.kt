package com.robosolutions.temixtopsmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentHomeBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.timer
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingViewModelFragment<FragmentHomeBinding, HomeFragmentViewModel>(),
    OnUserInteractionChangedListener {

    override val layoutId = R.layout.fragment_home

    override val useHeader = false

    private var logoClickCount = 0
    private lateinit var resetClickJob: Job

    private lateinit var autoReturnJob: Job
    override val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        greetUser()
    }

    private fun greetUser() = lifecycleScope.launchWhenCreated {
        mainViewModel.run {
            val greeting = greetingTts.singleLatest()

            if (greeting.isNotBlank()) mainViewModel.requestTts(greeting)
        }
    }

    fun onClickLogo(v: View) {
        logoClickCount++
        Timber.d("$logoClickCount/5 clicks for admin panel")

        when (logoClickCount) {
            1 -> resetClickJob = lifecycleScope.launch {
                delay(5000)

                Timber.d("Resets logo click count")
                logoClickCount = 0
            }
            5 -> v.navigate(R.id.action_homeFragment_to_passwordFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        logoClickCount = 0

        robot.addOnUserInteractionChangedListener(this)
    }

    override fun onPause() {
        super.onPause()

        if (::resetClickJob.isInitialized) resetClickJob.cancel()
        cancelAutoReturn()

        robot.removeOnUserInteractionChangedListener(this)
    }

    private fun cancelAutoReturn() {
        if (::autoReturnJob.isInitialized) {
            Timber.d("Stop auto return timer")
            viewModel.updateCountdown(-1)
            autoReturnJob.cancel()
        }
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        if (isInteracting) {
            greetUser()

            // Stop auto return timer
            cancelAutoReturn()
        } else {
            // Start auto return timer
            autoReturnJob = lifecycleScope.launch {
                val returnDelay = mainViewModel.autoReturnDelay.singleLatest() / 1000
                val returnLocation = mainViewModel.autoReturnLocation.singleLatest()

                Timber.d("Return location: $returnLocation")

                if (returnLocation == mainViewModel.lastLocation.value) {
                    Timber.d("Already on the location!")
                    return@launch
                }

                Timber.d("Start timer for $returnDelay seconds")

                timer(
                    returnDelay,
                    onElapse = viewModel::updateCountdown,
                    onTimesUp = { robot.goTo(returnLocation) }
                ).collect()
            }
        }
    }
}