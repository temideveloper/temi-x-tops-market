package com.robosolutions.temixtopsmarket.ui.activity

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.preference.PreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    repository: PreferenceRepository
) : ViewModel() {
    val detectRange = repository.general.map { it.detectionRange }.asLiveData()

    private val _ttsRequest = MutableSharedFlow<String>()
    val ttsRequest = _ttsRequest.asLiveData()

    fun requestTts(stringId: Int, vararg args: Any?) = viewModelScope.launch {
        if (ttsEngineReady.value) {
            _ttsRequest.emit(context.getString(stringId, *args))
        }
    }

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
                R.string.tts_complete_greeting
            } else {
                R.string.tts_partial_greeting
            }
        } else {
            -1
        }
    }
}