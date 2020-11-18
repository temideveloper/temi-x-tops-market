package com.robosolutions.temixtopsmarket.ui.general

import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentGeneralBinding
import com.robosolutions.temixtopsmarket.extensions.textString
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_general.*

@AndroidEntryPoint
class GeneralFragment :
    BindingViewModelFragment<FragmentGeneralBinding, GeneralFragmentViewModel>() {
    override val viewModel by viewModels<GeneralFragmentViewModel>()

    override val layoutId = R.layout.fragment_general

    override val titleIdEn = R.string.title_general_en
    override val titleIdThai = R.string.title_general_th

    override fun onPause() {
        super.onPause()

        viewModel.saveAutoReturnLocation(textAutoReturnLoc.textString)
    }
}