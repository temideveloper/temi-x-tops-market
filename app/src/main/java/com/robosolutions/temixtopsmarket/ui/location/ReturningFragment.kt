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

    /** `true` if navigation is aborted by the user. */
    private var userAbort = false

    override val showCloseButton = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robot.toggleNavigationBillboard(true)
        robot.addOnGoToLocationStatusChangedListener(this)
        robotReturn()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        robot.toggleNavigationBillboard(false)
        robot.removeOnGoToLocationStatusChangedListener(this)
    }

    private fun robotReturn() {
        val returnLocation = args.returnLocation

        if (returnLocation.isNotBlank()) {
            robot.goTo(returnLocation)
        } else {
            returnToHomeScreen()
        }
    }

    private fun returnToHomeScreen() {
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        requireActivity().onBackPressed()
    }

    /**
     * Called when the user tap the screen.
     *
     */
    fun onScreenTap() {
        userAbort = true
        robot.stopMovement()
        returnToHomeScreen()
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        when (status) {
            OnGoToLocationStatusChangedListener.ABORT -> if (!userAbort) robotReturn()
            OnGoToLocationStatusChangedListener.COMPLETE -> returnToHomeScreen()
        }
    }
}