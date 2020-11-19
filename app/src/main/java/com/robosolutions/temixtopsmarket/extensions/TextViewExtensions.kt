package com.robosolutions.temixtopsmarket.extensions

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.robosolutions.temixtopsmarket.R

/**
 * Sets the text without filtering the result. Suitable for dropdown selection.
 *
 * @param text The text to set.
 */
@BindingAdapter("textNoFilter")
fun AutoCompleteTextView.nonFilterText(text: String?) {
    text?.let { setText(it, false) }
}

/**
 * Populates the suggestions.
 *
 * @param content The suggestions.
 */
@BindingAdapter("content")
fun <T> AutoCompleteTextView.simpleAdapter(content: List<T>?) {
    setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, content ?: listOf()))
}

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

@BindingAdapter("onTextChange")
fun TextView.onTextChanged(block: TextChanged) {
    doOnTextChanged { text, _, _, _ -> block.onTextChange(text?.toString() ?: "") }
}

@BindingAdapter("textWithBracket")
fun TextView.bracketText(value: String?) {
    text = value?.let { "($it)" } ?: ""
}

interface TextChanged {
    fun onTextChange(text: String)
}