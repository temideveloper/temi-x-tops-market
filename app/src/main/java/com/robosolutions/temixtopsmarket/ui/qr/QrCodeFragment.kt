package com.robosolutions.temixtopsmarket.ui.qr

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentQrCodeBinding
import com.robosolutions.temixtopsmarket.extensions.getByFragmentResult
import com.robosolutions.temixtopsmarket.extensions.getUrl
import com.robosolutions.temixtopsmarket.extensions.textString
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_qr_code.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class QrCodeFragment : BindingViewModelFragment<FragmentQrCodeBinding, QrCodeFragmentViewModel>() {

    override val viewModel by viewModels<QrCodeFragmentViewModel>()

    override val layoutId = R.layout.fragment_qr_code

    override val titleIdEn = R.string.title_qr_code_en
    override val titleIdThai = R.string.title_qr_code_th

    fun scanThaiChana(v: View) = lifecycleScope.launch {
        getUrlFromScanFragment(v)?.let(viewModel::saveThaiChanaUrl)
    }

    fun scanPromotion(v: View) = lifecycleScope.launch {
        getUrlFromScanFragment(v)?.let(viewModel::savePromotionUrl)
    }

    override fun onPause() {
        super.onPause()

        viewModel.run {
            saveThaiChanaUrl(editTextThaiChana.textString)
            savePromotionUrl(editTextPromotion.textString)
        }
    }

    private suspend fun getUrlFromScanFragment(v: View): String? {
        val dir = QrCodeFragmentDirections.actionQrCodeFragmentToScanQrFragment()

        return getByFragmentResult(v, ScanQrDialog.REQUEST_KEY, dir) {
            it.getUrl().also { url ->
                Timber.d("Received URL from result: $url")
            }
        }
    }
}