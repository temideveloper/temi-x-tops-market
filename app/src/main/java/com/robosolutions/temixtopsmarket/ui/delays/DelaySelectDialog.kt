package com.robosolutions.temixtopsmarket.ui.delays

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.robosolutions.temixtopsmarket.BuildConfig
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentDelaySelectDialogBinding
import com.robosolutions.temixtopsmarket.extensions.putDuration
import com.robosolutions.temixtopsmarket.extensions.toDuration
import kotlinx.android.synthetic.main.fragment_delay_select_dialog.*

class DelaySelectDialog : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDelaySelectDialogBinding

    private val args by navArgs<DelaySelectDialogArgs>()

    private val duration by lazy { args.durationMs.toDuration() }

    val minutes by lazy { duration.second }
    val seconds by lazy { duration.third }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_delay_select_dialog,
            container,
            false
        )

        binding.fragment = this

        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                ?: return@setOnShowListener

            BottomSheetBehavior
                .from(bottomSheet)
                .state = BottomSheetBehavior.STATE_EXPANDED
        }

        return binding.root
    }

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