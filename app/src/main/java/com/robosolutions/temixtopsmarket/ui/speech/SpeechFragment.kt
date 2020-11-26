package com.robosolutions.temixtopsmarket.ui.speech

import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentSpeechBinding
import com.robosolutions.temixtopsmarket.extensions.textString
import com.robosolutions.temixtopsmarket.ui.base.BindingFragment
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_speech.*

@AndroidEntryPoint
class SpeechFragment : BindingFragment<FragmentSpeechBinding>() {

    override val layoutId = R.layout.fragment_speech

    override val titleIdEn = R.string.title_tts_en
    override val titleIdThai = R.string.title_tts_th
}