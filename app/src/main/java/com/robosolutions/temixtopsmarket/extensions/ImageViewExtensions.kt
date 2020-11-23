package com.robosolutions.temixtopsmarket.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import timber.log.Timber
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

@BindingAdapter("srcGlideId")
fun ImageView.glideSrcId(id: Int?) {
    id ?: return
    Glide.with(context)
        .load(id)
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

/**
 * Loads QR code from the [String].
 *
 * @param qrString The value of the QR code.
 */
@BindingAdapter("qrCodeString")
fun ImageView.qrCode(qrString: String?) {
    if (width == 0 || height == 0) {
        doOnGlobalLayout { loadQrCode(qrString) }
    } else {
        loadQrCode(qrString)
    }
}

private fun ImageView.loadQrCode(qrString: String?) {
    val qr = if (qrString != null && qrString.isNotBlank()) {
        qrString.toQrCode(width = width, height = height).also {
            Timber.d("Loading QR code with size $width x $height")
        }
    } else {
        ColorDrawable(Color.WHITE).toBitmap(width = width, height = height)
    }

    Glide.with(context)
        .load(qr)
        .into(this)
}