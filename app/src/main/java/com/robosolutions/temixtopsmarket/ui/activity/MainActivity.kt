package com.robosolutions.temixtopsmarket.ui.activity

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.ActivityMainBinding
import com.robosolutions.temixtopsmarket.extensions.completeHideTopBar
import com.robosolutions.temixtopsmarket.extensions.executePendingBindings
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.utils.isNightMode
import com.robosolutions.temixtopsmarket.utils.switchNightMode
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnRobotReadyListener, OnUserInteractionChangedListener {

    private val mainViewModel by viewModels<MainActivityViewModel>()

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.navHostFragment) }

    private val navigationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.homeFragment) {
                mainViewModel.updateHasNavigated(true)
            }
        }

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize tts client
        tts = TextToSpeech(this) { initStatus ->
            if (initStatus == TextToSpeech.SUCCESS) {
                if (tts.isLanguageAvailable(localeThai()) == TextToSpeech.LANG_AVAILABLE) {
                    Timber.d("Thai is supported!")
                    tts.language = localeThai()

                    mainViewModel.updateTtsEngine(true)
                } else {
                    Timber.w("Thai is not supported!")
                }
            } else {
                Timber.w("Failed to initialize tts! Please restart the application")
            }
        }
        
        robot.addOnRobotReadyListener(this)
        robot.addOnUserInteractionChangedListener(this)

        if (!isNightMode) switchNightMode(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.executePendingBindings {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }

        mainViewModel.detectRange.observe(this) { radius ->
            robot.setDetectionModeOn(true, radius)

            Timber.d("User detection set at $radius m")
        }

        mainViewModel.ttsRequest.observe(this) { message ->
            Timber.d("TTS: $message")
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString())
        }
    }

    override fun onResume() {
        super.onResume()

        navController.addOnDestinationChangedListener(navigationListener)
    }

    override fun onPause() {
        super.onPause()

        navController.removeOnDestinationChangedListener(navigationListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        robot.removeOnRobotReadyListener(this)
        robot.removeOnUserInteractionChangedListener(this)
    }

    private fun localeThai() = Locale.Builder()
        .setLanguage("th")
        .build()

    @Suppress("UNUSED_PARAMETER")
    fun onClickBack(v: View) = onBackPressed()

    @Suppress("UNUSED_PARAMETER")
    fun onClickHome(v: View) {
        navController.popBackStack(R.id.homeFragment, false)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (!isReady) return

        robot.completeHideTopBar(this)
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        Timber.d("User interaction update: $isInteracting")

        mainViewModel.updateIsInteracting(isInteracting)

        if (!isInteracting) {
            mainViewModel.updateHasNavigated(false)
        }
    }
}