package com.robosolutions.temixtopsmarket.extensions

import androidx.databinding.BindingAdapter
import com.google.android.material.slider.Slider

@BindingAdapter("onValueChange")
fun Slider.onValueChange(block: OnSliderChange) {
    addOnChangeListener { _, value, fromUser -> if (fromUser) block.onChange(value) }
}

interface OnSliderChange {
    fun onChange(value: Float)
}