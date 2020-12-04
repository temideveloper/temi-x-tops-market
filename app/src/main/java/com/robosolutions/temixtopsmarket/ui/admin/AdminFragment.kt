package com.robosolutions.temixtopsmarket.ui.admin

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentAdminBinding
import com.robosolutions.temixtopsmarket.extensions.completeHideTopBar
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.extensions.timer
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AdminFragment : BindingViewModelFragment<FragmentAdminBinding, AdminFragmentViewModel>() {
    override val layoutId = R.layout.fragment_admin

    override val viewModel by viewModels<AdminFragmentViewModel>()

    override val titleIdEn = R.string.title_admin_panel_en
    override val titleIdThai = R.string.title_admin_panel_th

    fun showTemiTopBar() = lifecycleScope.launch {
        robot.showTopBar()

        timer(
            seconds = 5,
            onElapse = viewModel::updateCountDown,
            onTimesUp = {
                robot.completeHideTopBar(requireActivity())
                viewModel.updateCountDown(-1)
            }
        ).collect()
    }

    fun sendToHomeBase() {
        robot.goTo("home base")
        requireActivity().onBackPressed()
    }

    override fun onStop() {
        super.onStop()

        robot.completeHideTopBar(requireActivity())
    }
}