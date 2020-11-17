package com.robosolutions.temixtopsmarket.ui.delays

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentDelaysBinding
import com.robosolutions.temixtopsmarket.extensions.getByFragmentResult
import com.robosolutions.temixtopsmarket.extensions.getDuration
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DelaysFragment : BindingViewModelFragment<FragmentDelaysBinding, DelaysFragmentViewModel>() {

    override val layoutId = R.layout.fragment_delays

    override val titleIdEn = R.string.title_delays_en
    override val titleIdThai = R.string.title_delays_th

    override val viewModel by viewModels<DelaysFragmentViewModel>()

    fun chooseAutoReturnDelay(v: View) = lifecycleScope.launch {
        val newDuration =
            getDurationFromDialog(v, viewModel.delays.value?.autoReturn) ?: return@launch

        viewModel.saveAutoReturnDelay(newDuration)
    }

    fun chooseCheckInReturnDelay(v: View) = lifecycleScope.launch {
        val newDuration =
            getDurationFromDialog(v, viewModel.delays.value?.checkInReturn) ?: return@launch

        viewModel.saveCheckInReturnDelay(newDuration)
    }

    fun chooseExcuseMeInterval(v: View) = lifecycleScope.launch {
        val newDuration =
            getDurationFromDialog(v, viewModel.delays.value?.excuseMeInterval) ?: return@launch

        viewModel.saveExcuseMeInterval(newDuration)
    }

    private suspend fun getDurationFromDialog(v: View, currentDurationMs: Int?) =
        currentDurationMs?.let {
            val dir =
                DelaysFragmentDirections.actionDelaysFragmentToDelaySelectDialog(it)

            getByFragmentResult(v, DelaySelectDialog.REQUEST_KEY, dir) { result ->
                result.getDuration()
            }
        }
}