package com.robosolutions.temixtopsmarket.ui.activity

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.extensions.updateTo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MainActivityViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
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
}