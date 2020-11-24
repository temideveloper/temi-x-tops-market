package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentReturningBinding
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.singleLatest
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReturningFragment :
    BindingViewModelFragment<FragmentReturningBinding, ReturningFragmentViewModel>(),
    OnGoToLocationStatusChangedListener {
    override val viewModel by viewModels<ReturningFragmentViewModel>()

    override val layoutId = R.layout.fragment_returning

    override val titleIdEn = R.string.title_navigation_en
    override val titleIdThai = R.string.title_navigation_th

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robot.toggleNavigationBillboard(true)
        robot.addOnGoToLocationStatusChangedListener(this)
        robotReturn()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        robot.toggleNavigationBillboard(false)
        robot.removeOnGoToLocationStatusChangedListener(this)
    }

    private fun robotReturn() = lifecycleScope.launch {
        val returnLocation = viewModel.returnLocation.singleLatest()

        if (returnLocation.isNotBlank()) {
            robot.goTo(returnLocation)
        } else {
            requireActivity().onBackPressed()
        }
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        when (status) {
            OnGoToLocationStatusChangedListener.ABORT -> robotReturn()
            OnGoToLocationStatusChangedListener.COMPLETE -> requireActivity().onBackPressed()
        }
    }
}