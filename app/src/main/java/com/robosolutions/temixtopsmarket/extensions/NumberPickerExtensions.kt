package com.robosolutions.temixtopsmarket.extensions

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter

@BindingAdapter("minValue")
fun NumberPicker.bindMinValue(value: Int) {
    minValue = value
}

@BindingAdapter("maxValue")
fun NumberPicker.bindMaxValue(value: Int) {
    maxValue = value
}

@BindingAdapter("value")
fun NumberPicker.bindValue(value: Int) {
    this.value = value
}