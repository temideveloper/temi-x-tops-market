package com.robosolutions.temixtopsmarket.ui.location

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentMapBinding
import com.robosolutions.temixtopsmarket.extensions.navigate
import com.robosolutions.temixtopsmarket.ui.activity.MainActivity
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BindingViewModelFragment<FragmentMapBinding, MapFragmentViewModel>() {

    override val viewModel by viewModels<MapFragmentViewModel>()

    override val layoutId = R.layout.fragment_map

    override val useHeader = false

    override val entranceSpeechId = R.string.tts_map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onStart() {
        super.onStart()

        if (mainViewModel.mapRevisited) {
            mainViewModel.requestTts(R.string.tts_map_revisit)
        }
    }

    fun onClickBack(v: View) {
        (requireActivity() as MainActivity).run { onClickBack(v) }
    }

    fun onSendRobotBack(v: View) {
        (requireActivity() as MainActivity).run { onSendRobotBack(v) }
    }

    fun onLocationSelected(v: View) {
        (v as Button).run {
            val location = text.toString()

            val dir = MapFragmentDirections.actionMapFragmentToNavigatingFragment(location)
            navigate(dir)
        }
    }
}