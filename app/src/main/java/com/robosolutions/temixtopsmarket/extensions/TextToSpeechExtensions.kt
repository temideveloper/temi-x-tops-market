package com.robosolutions.temixtopsmarket.extensions

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun TextToSpeech.speakAndWait(
    text: String,
    queueMode: Int,
    param: Bundle? = null,
    speechId: String = UUID.randomUUID().toString()
) = suspendCoroutine<Unit> {
    setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) = Unit

        override fun onDone(utteranceId: String?) {
            if (utteranceId == speechId) {
                it.resume(Unit)
            }
        }

        override fun onError(utteranceId: String?) {
            if (utteranceId == speechId) {
                Timber.w("Encountered error when trying tts with id $utteranceId")
                it.resume(Unit)
            }
        }
    })

    speak(text, queueMode, param, speechId)
}