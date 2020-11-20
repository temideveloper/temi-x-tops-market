package com.robosolutions.temixtopsmarket.extensions

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

/**
 * Returns `true` if the [String] contains both a lowercase character and an uppercase character.
 *
 */
fun String.isMixedCase() = contains(Regex("[A-Z]")) && contains(Regex("[a-z]"))

/**
 * Converts the [String] content into a QR code [Bitmap].
 *
 * @param width The width of the [Bitmap].
 * @param height The height of the [Bitmap].
 *
 * @return The QR code image.
 */
fun String.toQrCode(width: Int, height: Int): Bitmap =
    MultiFormatWriter().encode(
        this,
        BarcodeFormat.QR_CODE,
        width,
        height
    ).run {
        BarcodeEncoder().createBitmap(this)
    }