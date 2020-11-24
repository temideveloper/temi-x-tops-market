package com.robosolutions.temixtopsmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentHomeBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.ui.base.BindingFragment
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : BindingFragment<FragmentHomeBinding>(), OnUserInteractionChangedListener {

    override val layoutId = R.layout.fragment_home

    override val useHeader = false

    private var logoClickCount = 0
    private lateinit var resetClickJob: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        greetUser()
    }

    private fun greetUser() = lifecycleScope.launchWhenCreated {
        mainViewModel.run {
            val greetingId = greetingTts.singleLatest()

            if (greetingId != -1) mainViewModel.requestTts(greetingId)
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
        robot.removeOnUserInteractionChangedListener(this)
    }


    override fun onUserInteraction(isInteracting: Boolean) {
        if (isInteracting) {
            greetUser()
        }
    }
}