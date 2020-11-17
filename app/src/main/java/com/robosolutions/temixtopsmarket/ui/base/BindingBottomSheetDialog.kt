package com.robosolutions.temixtopsmarket.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.utils.tryAssignBinding

abstract class BindingBottomSheetDialog<T : ViewDataBinding> : BottomSheetDialogFragment() {
    protected lateinit var binding: T

    abstract val layoutId: Int

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        tryAssignBinding("setFragment", this::class.java, binding, this)

        // Expand dialog on start
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
}