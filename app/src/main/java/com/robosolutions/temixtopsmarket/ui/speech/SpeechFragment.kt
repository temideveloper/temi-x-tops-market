package com.robosolutions.temixtopsmarket.ui.speech

import androidx.fragment.app.viewModels
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.FragmentSpeechBinding
import com.robosolutions.temixtopsmarket.extensions.textString
import com.robosolutions.temixtopsmarket.ui.base.BindingViewModelFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_speech.*

@AndroidEntryPoint
class SpeechFragment : BindingViewModelFragment<FragmentSpeechBinding, SpeechFragmentViewModel>() {

    override val viewModel by viewModels<SpeechFragmentViewModel>()

    override val layoutId = R.layout.fragment_speech

    override val titleIdEn = R.string.title_tts_en
    override val titleIdThai = R.string.title_tts_th

    override fun onPause() {
        super.onPause()

        viewModel.run {
            saveGreeting(editTextGreeting.textString)
            saveExcuseMe(editTextExcuseMe.textString)
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

//        tts = TextToSpeech(requireContext().applicationContext) { status ->
//            if (status != TextToSpeech.SUCCESS) {
//                return@TextToSpeech
//            }
//
//            if (tts.isLanguageAvailable(localeThai()) == TextToSpeech.LANG_AVAILABLE) {
//                Timber.d("Thai is supported!")
//                tts.language = localeThai()
//                tts.speak("สวัสดีชาวโลก!", TextToSpeech.QUEUE_FLUSH, null, "abc")
//            } else {
//                Timber.d("Thai is not supported!")
//            }
//        }
}

//    private fun localeThai() = Locale.Builder()
//        .setLanguage("th")
//        .build()
//}