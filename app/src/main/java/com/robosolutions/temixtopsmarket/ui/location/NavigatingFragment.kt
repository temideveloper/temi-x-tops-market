package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentNavigatingBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.ui.base.BindingFragment
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import kotlinx.android.synthetic.main.fragment_navigating.*
import timber.log.Timber

class NavigatingFragment : BindingFragment<FragmentNavigatingBinding>(),
    OnGoToLocationStatusChangedListener {
    override val layoutId = R.layout.fragment_navigating

    override val titleIdEn = R.string.title_navigation_en
    override val titleIdThai = R.string.title_navigation_th

    val args by navArgs<NavigatingFragmentArgs>()

    override val entranceSpeechId = R.string.tts_navigating
    override val entranceSpeechArgs: Array<Any?>? by lazy { arrayOf(args.location) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robot.toggleNavigationBillboard(true)
        robot.addOnGoToLocationStatusChangedListener(this)
        robot.goTo(args.location)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        robot.toggleNavigationBillboard(false)
        robot.removeOnGoToLocationStatusChangedListener(this)
        robot.stopMovement()
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        when (status) {
            OnGoToLocationStatusChangedListener.COMPLETE -> {
                Timber.d("Arrived at $location")

                val dir =
                    NavigatingFragmentDirections.actionNavigatingFragmentToArrivedFragment(args.location)
                parentLayout.navigate(dir)
            }
            OnGoToLocationStatusChangedListener.ABORT -> {
                Timber.d("Navigation aborted! Retrying")
                robot.goTo(args.location)
            }
        }
    }
}