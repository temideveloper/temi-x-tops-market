package com.robosolutions.temixtopsmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentHomeBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.ui.activity.MainActivity
import com.robosolutions.temixtopsmarket.ui.base.BindingFragment
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(), OnUserInteractionChangedListener {

    override val layoutId = R.layout.fragment_home

    override val useHeader = false

    private var logoClickCount = 0
    private lateinit var resetClickJob: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        greetUser()

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
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

    fun onSendRobotBack(v: View) {
        (requireActivity() as MainActivity).onSendRobotBack(v)
    }

    override fun onResume() {
        super.onResume()

        logoClickCount = 0
        mainViewModel.mapRevisited = false

        robot.addOnUserInteractionChangedListener(this)
    }

    override fun onPause() {
        super.onPause()

        if (::resetClickJob.isInitialized) resetClickJob.cancel()
        robot.removeOnUserInteractionChangedListener(this)
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        if (isInteracting) {
            greetUser()
        }
    }
}