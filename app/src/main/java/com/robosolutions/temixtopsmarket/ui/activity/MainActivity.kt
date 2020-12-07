package com.robosolutions.temixtopsmarket.ui.activity

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.ActivityMainBinding
import com.robosolutions.temixtopsmarket.extensions.*
import com.robosolutions.temixtopsmarket.ui.checkin.CheckInFragmentDirections
import com.robosolutions.temixtopsmarket.ui.home.HomeFragmentDirections
import com.robosolutions.temixtopsmarket.ui.location.ArrivedFragmentDirections
import com.robosolutions.temixtopsmarket.ui.location.MapFragmentDirections
import com.robosolutions.temixtopsmarket.ui.staff.ContactStaffFragmentDirections
import com.robosolutions.temixtopsmarket.ui.video.PromotionFragmentDirections
import com.robosolutions.temixtopsmarket.utils.isNightMode
import com.robosolutions.temixtopsmarket.utils.switchNightMode
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.permission.OnRequestPermissionResultListener
import com.robotemi.sdk.permission.Permission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    OnRobotReadyListener,
    OnUserInteractionChangedListener,
    OnGoToLocationStatusChangedListener,
    OnRequestPermissionResultListener {

    private val mainViewModel by viewModels<MainActivityViewModel>()

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy { findNavController(R.id.navHostFragment) }

    private val currentDestinationId
        get() = navController.currentDestination?.id ?: -1

    private val navigationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.homeFragment) {
                mainViewModel.updateHasNavigated(true)
            }
        }

    private lateinit var autoReturnJob: Job
    private lateinit var autoReturnDialog: AlertDialog

    /** List of destination ids that the auto return dialog should not show. */
    private val autoReturnInclusionList = listOf(
        R.id.homeFragment,
        R.id.contactStaffFragment,
        R.id.mapFragment,
        R.id.arrivedFragment
    )

    /**
     * Stops auto return timer.
     *
     */
    private fun cancelAutoReturn() {
        if (::autoReturnJob.isInitialized) {
            Timber.d("Stop auto return timer")
            autoReturnJob.cancel()
        }

        if (::autoReturnDialog.isInitialized) {
            autoReturnDialog.dismiss()
        }
    }

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
        robot.addOnRequestPermissionResultListener(this)
        robot.addOnUserInteractionChangedListener(this)
        robot.addOnGoToLocationStatusChangedListener(this)

        if (!isNightMode) switchNightMode(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.executePendingBindings {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }

        mainViewModel.run {
            detectRange.asLiveData().observe(this@MainActivity) {
                robot.setDetectionModeOn(true, it)
                Timber.d("User detection set at $it m")
            }

            ttsRequest.observe(this@MainActivity) { message ->
                Timber.d("TTS: $message")

                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString())
            }

            snackBarRequest.observe(this@MainActivity) { message ->
                Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                    .setAction(android.R.string.ok) { /* dismiss on click */ }
                    .show()
            }

            exactUserInteraction.observe(this@MainActivity) { userInteracts ->
                if (mainViewModel.isGoing.value) return@observe

                Timber.d("Exact user interaction: $userInteracts")

                if (userInteracts) {
                    robot.stopMovement()
                } else {
                    // Show only if the current destination is not in the exclusion list
                    if (currentDestinationId !in autoReturnInclusionList) return@observe

                    startAutoReturnDialog()
                }
            }
        }
    }

    /**
     * Shows the auto-return dialog. If there is one showing already, it will restart the dialog.
     *
     * @param onCancel Additional action to do upon cancel (user taps the scrim area).
     * @param onWait Additional action to do when user taps "wait".
     */
    fun startAutoReturnDialog(onCancel: () -> Unit = {}, onWait: () -> Unit = {}) {
        // Ensure only 1 kind of this task is running
        cancelAutoReturn()

        autoReturnJob = lifecycleScope.launch {
            val returnDelay = mainViewModel.autoReturnDelay.singleLatest() / 1000
            val returnLocation = mainViewModel.autoReturnLocation.singleLatest()

            Timber.d("Return location: $returnLocation")

            if (returnLocation == mainViewModel.lastLocation.value) {
                Timber.d("Already on the location!")
                return@launch
            }

            if (mainViewModel.isGoing.value) {
                Timber.d("Temi is going! Cannot display dialog")
                return@launch
            }

            // Show dialog and speak
            autoReturnDialog =
                MaterialAlertDialogBuilder(this@MainActivity, R.style.AutoReturnAlertDialog)
                    .setIcon(R.drawable.ic_info)
                    .setTitle(" ")
                    .setMessage("")
                    .setNeutralButton(R.string.button_send_back) { _, _ ->
                        onSendRobotBack(navHostFragment)
                    }
                    .setPositiveButton(R.string.button_wait) { _, _ ->
                        cancelAutoReturn()
                        onWait()
                    }
                    .setOnCancelListener {
                        cancelAutoReturn()
                        onCancel()
                    }
                    .create()

            autoReturnDialog.show()
            mainViewModel.requestTts(R.string.tts_no_activity, returnDelay)

            // Start the timer
            Timber.d("Start timer for $returnDelay seconds")
            timer(
                returnDelay,
                onElapse = {
                    val dialogMessage =
                        getString(R.string.dialog_content_auto_return).format(it)

                    autoReturnDialog.setMessage(dialogMessage)
                },
                onTimesUp = {
                    autoReturnDialog.dismiss()
                    onSendRobotBack(navHostFragment)
                }
            ).collect()
        }
    }

    override fun onResume() {
        super.onResume()

        navController.addOnDestinationChangedListener(navigationListener)
    }

    override fun onPause() {
        super.onPause()

        cancelAutoReturn()
        navController.removeOnDestinationChangedListener(navigationListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        robot.removeOnRobotReadyListener(this)
        robot.removeOnUserInteractionChangedListener(this)
        robot.removeOnGoToLocationStatusChangedListener(this)
        robot.removeOnRequestPermissionResultListener(this)
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

    /**
     * Sends robot back to the chosen auto-return location.
     * Also brings the screen back to the ome screen.
     *
     * @param v Any view.
     */
    @Suppress("UNUSED_PARAMETER")
    fun onSendRobotBack(v: View) {
        lifecycleScope.launch {
            mainViewModel.autoReturnLocation.singleLatest().let {
                val dir = when (currentDestinationId) {
                    R.id.homeFragment ->
                        HomeFragmentDirections.actionHomeFragmentToReturningFragment(it)

                    R.id.promotionFragment ->
                        PromotionFragmentDirections.actionPromotionFragmentToReturningFragment(it)

                    R.id.mapFragment ->
                        MapFragmentDirections.actionMapFragmentToReturningFragment(it)

                    R.id.contactStaffFragment ->
                        ContactStaffFragmentDirections
                            .actionContactStaffFragmentToReturningFragment(it)

                    R.id.arrivedFragment ->
                        ArrivedFragmentDirections.actionArrivedFragmentToReturningFragment(it)

                    R.id.checkInFragment ->
                        CheckInFragmentDirections.actionCheckInFragmentToReturningFragment(it)

                    else -> return@launch
                }

                navHostFragment.navigate(dir)
            }
        }
    }

    override fun onRobotReady(isReady: Boolean) {
        if (!isReady) return

        robot.completeHideTopBar(this)

        val permissionGranted = robot.checkSelfPermission(Permission.SETTINGS) == Permission.GRANTED

        mainViewModel.updateSettingsPermission(permissionGranted)

        if (!permissionGranted) {
            robot.requestPermissions(listOf(Permission.SETTINGS), REQUEST_CODE)
        }
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        Timber.d("User interaction update: $isInteracting")

        mainViewModel.updateIsInteracting(isInteracting)

        if (!isInteracting) {
            mainViewModel.updateHasNavigated(false)
        }
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        when (status) {
            OnGoToLocationStatusChangedListener.START -> mainViewModel.setTemiGoing(true)
            OnGoToLocationStatusChangedListener.COMPLETE -> {
                mainViewModel.updateLastLocation(location)
                mainViewModel.setTemiGoing(false)
            }
            OnGoToLocationStatusChangedListener.ABORT -> mainViewModel.setTemiGoing(false)
            OnGoToLocationStatusChangedListener.GOING -> mainViewModel.updateLastLocation("")
        }
    }

    override fun onRequestPermissionResult(
        permission: Permission,
        grantResult: Int,
        requestCode: Int
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResult == Permission.DENIED) {
                Timber.d("Needs settings permission, but denied!")

                mainViewModel.requestSnackBar(R.string.snack_bar_settings_permission_denied)
            } else {
                Timber.d("Settings permission accepted")
            }

            mainViewModel.updateSettingsPermission(grantResult == Permission.GRANTED)
        }
    }

    companion object {
        const val REQUEST_CODE = 1042
        lateinit var tts: TextToSpeech
    }
}