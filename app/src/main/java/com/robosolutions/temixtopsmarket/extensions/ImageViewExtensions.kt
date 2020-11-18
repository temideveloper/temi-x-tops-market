package com.robosolutions.temixtopsmarket.extensions

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["srcGlideUrl", "fallbackId"], requireAll = false)
fun ImageView.glideUrlSrc(url: String?, fallback: Drawable?) {
    Glide.with(context)
        .load(url?.let { Uri.parse(url) } /* ?: fallback */)
        .placeholder(fallback)
        .error(fallback)
        .fallback(fallback)
        .into(this)
}