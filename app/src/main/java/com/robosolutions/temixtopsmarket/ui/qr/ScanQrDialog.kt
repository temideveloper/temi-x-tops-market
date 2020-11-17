package com.robosolutions.temixtopsmarket.ui.qr

import android.Manifest
import android.graphics.ImageFormat
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.robosolutions.temixtopsmarket.BuildConfig
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentScanQrBinding
import com.robosolutions.temixtopsmarket.extensions.putUrl
import com.robosolutions.temixtopsmarket.extensions.textString
import com.robosolutions.temixtopsmarket.ui.base.BindingBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_scan_qr.*
import javax.inject.Inject

@AndroidEntryPoint
class ScanQrDialog : BindingBottomSheetDialog<FragmentScanQrBinding>() {

    val viewModel by viewModels<ScanQrDialogViewModel>()

    @Inject
    lateinit var barcodeScanner: BarcodeScanner

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                requireActivity().onBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request for camera permission
        requestPermission.launch(Manifest.permission.CAMERA)
    }

    override val layoutId = R.layout.fragment_scan_qr

    private fun startCamera() {
        cameraView.apply {
            setLifecycleOwner(viewLifecycleOwner)

            addFrameProcessor {
                if (it.dataClass == ByteArray::class.java) {
                    val input = InputImage.fromByteArray(
                        it.getData<ByteArray>(),
                        it.size.width,
                        it.size.height,
                        it.rotationToView,
                        ImageFormat.NV21
                    )

                    val scanResult = Tasks.await(barcodeScanner.process(input)).firstOrNull()

                    scanResult?.url?.url?.let(viewModel::updateDetectedUrl)
                }
            }
        }
    }

    fun onConfirmUrl() {
        val data = Bundle().apply { putUrl(textDetectedUrl.textString) }

        setFragmentResult(REQUEST_KEY, data)

        dismiss()
    }

    companion object {
        const val REQUEST_KEY = BuildConfig.APPLICATION_ID + "_REQUEST_URL"
    }
}