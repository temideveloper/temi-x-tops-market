package com.robosolutions.temixtopsmarket.ui.admin

import androidx.lifecycle.asLiveData
import com.robosolutions.temixtopsmarket.extensions.updateTo
import com.robosolutions.temixtopsmarket.ui.base.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AdminFragmentViewModel : AppViewModel() {
    private val countDown = MutableStateFlow(-1)
    val currentCount = countDown.asLiveData()

    fun updateCountDown(count: Int) = countDown updateTo count
}