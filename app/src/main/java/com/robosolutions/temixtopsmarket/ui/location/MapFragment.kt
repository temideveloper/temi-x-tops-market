package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentMapBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.extensions.robot
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import com.robotemi.sdk.permission.OnRequestPermissionResultListener
import com.robotemi.sdk.permission.Permission
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : BindingViewModelFragment<FragmentMapBinding, MapFragmentViewModel>(),
    OnRequestPermissionResultListener {

    override val viewModel by viewModels<MapFragmentViewModel>()

    override val layoutId = R.layout.fragment_map

    override val titleIdEn = R.string.title_store_navigation_en
    override val titleIdThai = R.string.title_store_navigation_th

    override val entranceSpeechId = R.string.tts_map

    override fun onBinding(binding: FragmentMapBinding) {
        super.onBinding(binding)

        robot.addOnRequestPermissionResultListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (robot.checkSelfPermission(Permission.SETTINGS) != Permission.GRANTED) {
            robot.requestPermissions(listOf(Permission.SETTINGS), REQUEST_CODE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        robot.removeOnRequestPermissionResultListener(this)
    }

    fun onLocationSelected(v: View) {
        (v as Button).run {
            val location = text.toString()

            val dir = MapFragmentDirections.actionMapFragmentToNavigatingFragment(location)
            navigate(dir)
        }
    }

    override fun onRequestPermissionResult(
        permission: Permission,
        grantResult: Int,
        requestCode: Int
    ) {
        if (grantResult == Permission.DENIED) {
            Timber.d("Needs settings permission, but denied!")

            requireActivity().onBackPressed()
        }
    }

    companion object {
        const val REQUEST_CODE = 1042
    }
}