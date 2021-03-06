package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentReturningBinding
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.ui.base.BindingFragment
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReturningFragment : BindingFragment<FragmentReturningBinding>(),
    OnGoToLocationStatusChangedListener {

    private val args by navArgs<ReturningFragmentArgs>()

    override val layoutId = R.layout.fragment_returning

    override val titleIdEn = R.string.title_navigation_en
    override val titleIdThai = R.string.title_navigation_th

    override val entranceSpeechId = R.string.tts_returning
    override val entranceSpeechArgs: Array<Any?> by lazy { arrayOf(args.returnLocation) }

    override val showCloseButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
    }

    override fun onStart() {
        super.onStart()

        robot.toggleNavigationBillboard(true)
        robot.addOnGoToLocationStatusChangedListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robotReturn()
    }

    override fun onStop() {
        super.onStop()

        robot.toggleNavigationBillboard(false)
        robot.removeOnGoToLocationStatusChangedListener(this)
        robot.stopMovement()
    }

    private fun robotReturn() {
        val returnLocation = args.returnLocation

        if (returnLocation.isNotBlank()) {
            robot.goTo(returnLocation)
        } else {
            returnToHomeScreen()
        }
    }

    fun returnToHomeScreen() {
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        requireActivity().onBackPressed()
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        when (status) {
            OnGoToLocationStatusChangedListener.ABORT -> robotReturn()
            OnGoToLocationStatusChangedListener.COMPLETE -> returnToHomeScreen()
        }
    }
}