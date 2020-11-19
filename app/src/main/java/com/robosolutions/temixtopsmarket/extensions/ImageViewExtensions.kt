package com.robosolutions.temixtopsmarket.extensions

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import java.io.File

@BindingAdapter(value = ["srcGlideUrl", "fallbackId"])
fun ImageView.glideUrlSrc(url: String?, fallback: Drawable) {
    Glide.with(context)
        .load(url?.let { Uri.parse(url) })
        .placeholder(fallback)
        .error(fallback)
        .fallback(fallback)
        .into(this)
}

/**
 * Loads thumbnail image for a video.
 *
 * @param file The video file.
 * @param fallback The fallback thumbnail to use.
 */
@BindingAdapter(value = ["srcGlideThumb", "fallbackId"])
fun ImageView.glideThumbnail(file: File?, fallback: Drawable) {
    Glide.with(context)
        .asBitmap()
        .load(file)
        .placeholder(fallback)
        .error(fallback)
        .fallback(fallback)
        .signature(ObjectKey(file?.lastModified() ?: 0L))
        .into(this)
}