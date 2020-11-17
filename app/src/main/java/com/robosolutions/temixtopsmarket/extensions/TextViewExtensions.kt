package com.robosolutions.temixtopsmarket.extensions

import android.content.Context
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.robosolutions.temixtopsmarket.R

/**
 * The current text as a [String]. If there is no text, an empty [String] is returned.
 */
val TextView.textString
    get() = text?.toString() ?: ""

/**
 * Sets the text for duration.
 *
 * @param ms The duration in milliseconds.
 * @param default The default duration in milliseconds.
 */
@BindingAdapter("textDuration", "textDurationDefault")
fun TextView.durationText(ms: Int?, default: Int) {
    text = ms?.let {
        getDurationString(it, context)
    } ?: getDurationString(default, context)
}

private fun getDurationString(totalMs: Int, context: Context): String {
    val (_, minutes, seconds) = totalMs.toDuration()

    return context.getString(R.string.label_duration).format(minutes, seconds)
}