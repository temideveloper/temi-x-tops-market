package com.robosolutions.temixtopsmarket.ui.activity

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.extensions.combineToPair
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MainActivityViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: PreferenceRepository,
) : AppViewModel() {
    private val hasSettingsPermission = MutableStateFlow(false)

    fun updateSettingsPermission(isGranted: Boolean) = hasSettingsPermission updateTo isGranted

    // General Settings
    private val _general = repository.general

    val detectRange = _general.map { it.detectionRange }.combineToPair(hasSettingsPermission)
        .filter { (_, hasPermission) -> hasPermission }
        .map { (range, _) -> range }

    val autoReturnLocation = _general.map { it.autoReturnLocation }

    val general = _general.asLiveData()

    fun saveDetectionRange(range: Float) = viewModelLaunch { repository.saveDetectionRange(range) }

    fun saveAutoReturnLocation(location: String) =
        viewModelLaunch { repository.saveAutoReturnLocation(location) }

    // Delay Settings
    private val _delays = repository.delays

    val autoReturnDelay = _delays.map { it.autoReturn }

    val excuseMeDelay = _delays.map { it.excuseMeInterval }

    val delays = _delays.asLiveData()

    fun saveAutoReturnDelay(delayMs: Int) =
        viewModelLaunch { repository.saveAutoReturnDelay(delayMs) }

    fun saveCheckInReturnDelay(delayMs: Int) =
        viewModelLaunch { repository.saveCheckInReturnDelay(delayMs) }

    fun saveExcuseMeInterval(delayMs: Int) =
        viewModelLaunch { repository.saveExcuseMeInterval(delayMs) }

    // Speech Settings

    private val _speech = repository.speech
    val speech = _speech.asLiveData()

    fun saveGreeting(message: String) = viewModelLaunch { repository.saveGreeting(message) }

    fun saveRecurringGreeting(message: String) =
        viewModelLaunch { repository.saveRecurringGreeting(message) }

    private val _ttsRequest = MutableSharedFlow<String>()
    val ttsRequest = _ttsRequest.asLiveData()

    fun requestTts(stringId: Int, vararg args: Any?) =
        requestTts(context.getString(stringId), *args)

    fun requestTts(message: String, vararg args: Any?) = viewModelScope.launch {
        if (ttsEngineReady.value) {
            _ttsRequest.emit(message.format(*args))
        }
    }

    private val _snackBarRequest = MutableSharedFlow<String>()
    val snackBarRequest = _snackBarRequest.asLiveData()

    fun requestSnackBar(id: Int) = _snackBarRequest launchAndEmit context.getString(id)

    private val _displayHeader = MutableStateFlow(false)
    val displayHeader = _displayHeader.asLiveData()

    private val _titleEnglish = MutableStateFlow("")
    val titleEnglish = _titleEnglish.asLiveData()

    private val _titleThai = MutableStateFlow("")
    val titleThai = _titleThai.asLiveData()

    private val _headerImageId = MutableStateFlow<Int?>(null)
    val headerImageId = _headerImageId.asLiveData()

    private val _contentPadding = MutableStateFlow(R.dimen.zero_dp)
    val contentPadding = _contentPadding.map { context.resources.getDimension(it) }.asLiveData()

    fun updateHeader(isVisible: Boolean) = _displayHeader updateTo isVisible

    fun updateHeaderTitle(englishId: Int?, thaiId: Int?) {
        updateHeaderEnglish(englishId?.let { context.getString(it) } ?: "")
        updateHeaderThai(thaiId?.let { context.getString(it) } ?: "")
    }

    fun updateHeaderThai(title: String) = _titleThai updateTo title

    fun updateHeaderEnglish(title: String) = _titleEnglish updateTo title

    fun updateHeaderImageId(id: Int?) = _headerImageId updateTo id

    fun updateContentPadding(dimenId: Int?) = _contentPadding updateTo (dimenId ?: R.dimen.zero_dp)

    private val _showCloseButton = MutableStateFlow(true)
    val showCloseButton = _showCloseButton.asLiveData()

    fun showCloseButton(show: Boolean) = _showCloseButton updateTo show

    private val _showHomeButton = MutableStateFlow(true)
    val showHomeButton = _showHomeButton.asLiveData()

    fun showHomeButton(show: Boolean) = _showHomeButton updateTo show

    private val _showSendBackButton = MutableStateFlow(false)
    val showSendBackButton = _showSendBackButton.asLiveData()

    fun showSendBackButton(show: Boolean) = _showSendBackButton updateTo show

    private val isInteracting = MutableStateFlow(true)
    private val hasNavigatedScreen = MutableStateFlow(false)

    /** Whether temi should greet the new user or not. */
    private val _shouldGreetUser =
        isInteracting.combine(hasNavigatedScreen) { interacting, navigated ->
            interacting && !navigated
        }

    /** The welcome message is displayed when the user should be greeted or there is no user. */
    val shouldShowGreetMessage =
        _shouldGreetUser.combine(isInteracting) { shouldGreet, interacting ->
            shouldGreet || !interacting
        }.asLiveData()

    fun updateIsInteracting(interacting: Boolean) = isInteracting updateTo interacting


    fun updateHasNavigated(navigated: Boolean) = hasNavigatedScreen updateTo navigated

    private val ttsEngineReady = MutableStateFlow(false)

    fun updateTtsEngine(ready: Boolean) = ttsEngineReady updateTo ready

    val greetingTts = isInteracting.combine(hasNavigatedScreen) { interacting, navigated ->
        if (interacting) {
            if (!navigated) {
                _speech.map { it.greeting }.singleLatest()
            } else {
                _speech.map { it.recurringGreeting }.singleLatest()
            }
        } else {
            ""
        }
    }

    private val _lastLocation = MutableStateFlow("")
    val lastLocation: StateFlow<String> = _lastLocation

    fun updateLastLocation(last: String) = _lastLocation updateTo last

    private val _isGoing = MutableStateFlow(false)
    val isGoing: StateFlow<Boolean> = _isGoing

    fun setTemiGoing(going: Boolean) = _isGoing updateTo going

    /** Emits the user interaction changes only because of the user.
     * This is because temi will change the user interaction to
     * `true` when the robot is going to a location. */
    val exactUserInteraction = isInteracting.combineToPair(_isGoing)
        .filter { (_, going) -> !going }
        .map { (interaction, _) -> interaction }
        .debounce(1000)
        .distinctUntilChanged()
        .asLiveData()

    // Map fragment state
    var mapRevisited = false
}