package com.robosolutions.temixtopsmarket.ui.delays

import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.robosolutions.temixtopsmarket.BuildConfig
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentDelaySelectDialogBinding
import com.robosolutions.temixtopsmarket.extensions.putDuration
import com.robosolutions.temixtopsmarket.extensions.toDuration
import com.robosolutions.temixtopsmarket.ui.base.BindingBottomSheetDialog
import kotlinx.android.synthetic.main.fragment_delay_select_dialog.*

class DelaySelectDialog : BindingBottomSheetDialog<FragmentDelaySelectDialogBinding>() {
    private val args by navArgs<DelaySelectDialogArgs>()

    private val duration by lazy { args.durationMs.toDuration() }

    val minutes by lazy { duration.second }
    val seconds by lazy { duration.third }

    override val layoutId = R.layout.fragment_delay_select_dialog

    /**
     * Returns the chosen duration in milliseconds.
     *
     */
    fun onConfirmClicked() {
        val minute = pickerMinutes.value
        val second = pickerSeconds.value

        val data = Bundle().apply {
            putDuration((minute * 60 + second) * 1000)
        }

        setFragmentResult(REQUEST_KEY, data)

        dismiss()
    }

    companion object {
        const val REQUEST_KEY = BuildConfig.APPLICATION_ID + "_REQUEST_DURATION"
    }
}