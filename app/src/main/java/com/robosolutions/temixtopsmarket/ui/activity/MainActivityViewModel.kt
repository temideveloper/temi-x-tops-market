package com.robosolutions.temixtopsmarket.ui.activity

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.updateTo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _displayHeader = MutableStateFlow(false)
    val displayHeader = _displayHeader.asLiveData()

    private val _titleEnglish = MutableStateFlow("")
    val titleEnglish = _titleEnglish.asLiveData()

    private val _titleThai = MutableStateFlow("")
    val titleThai = _titleThai.asLiveData()

    fun updateHeader(isVisible: Boolean) = _displayHeader updateTo isVisible

    fun updateHeaderTitle(englishId: Int?, thaiId: Int?) {
        _titleEnglish updateTo (englishId?.let { context.getString(it) } ?: "")
        _titleThai updateTo (thaiId?.let { context.getString(it) } ?: "")
    }
}