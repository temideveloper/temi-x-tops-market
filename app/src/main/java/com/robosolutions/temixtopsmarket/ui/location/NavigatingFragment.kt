package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialFadeThrough
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentNavigatingBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.speakAndWait
import com.robosolutions.temixtopsmarket.ui.activity.MainActivity
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import kotlinx.android.synthetic.main.fragment_navigating.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.util.*

class NavigatingFragment :
    BindingViewModelFragment<FragmentNavigatingBinding, NavigatingFragmentViewModel>(),
    OnGoToLocationStatusChangedListener {
    override val layoutId = R.layout.fragment_navigating

    override val viewModel by viewModels<NavigatingFragmentViewModel>()

    override val titleIdEn = R.string.title_navigation_en
    override val titleIdThai = R.string.title_navigation_th

    val args by navArgs<NavigatingFragmentArgs>()
    private val goToLocation by lazy { args.location.toLowerCase(Locale.ROOT) }

    override val entranceSpeechId = R.string.tts_navigating
    override val entranceSpeechArgs: Array<Any?>? by lazy { arrayOf(args.location) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onStart() {
        super.onStart()

        robot.toggleNavigationBillboard(true)
        robot.addOnGoToLocationStatusChangedListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robot.goTo(goToLocation)

        lifecycleScope.launchWhenCreated {
            viewModel.latestGoToDesc.collectLatest {
                val delayMs = mainViewModel.excuseMeDelay.singleLatest()

                val message = when {
                    it.contains("obstacle", true) ->
                        requireContext().getString(R.string.tts_excuse_me)

                    it.contains("path plan", true) ->
                        requireContext().getString(R.string.tts_path_plan)

                    else -> return@collectLatest
                }

                while (true) {
                    ensureActive()

                    Timber.d("Speaking")
                    MainActivity.tts.speakAndWait(message, TextToSpeech.QUEUE_FLUSH)
                    Timber.d("Spoken")

                    Timber.d("Delaying")
                    delay(delayMs.toLong())
                    Timber.d("Delayed")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

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
        Timber.d("Description ($descriptionId): $description")
        viewModel.updateGoToDescription(description)

        when (status) {
            OnGoToLocationStatusChangedListener.COMPLETE -> {
                Timber.d("Arrived at $location")

                val dir =
                    NavigatingFragmentDirections.actionNavigatingFragmentToArrivedFragment(args.location)
                parentLayout.navigate(dir)
            }

            OnGoToLocationStatusChangedListener.ABORT -> {
                Timber.d("Navigation aborted! Retrying")
                robot.goTo(goToLocation)
            }
        }
    }
}