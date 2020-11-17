package com.robosolutions.temixtopsmarket.extensions

/**
 * Converts this milliseconds integer into hours, minutes, and seconds.
 *
 * @return Hours, minutes, seconds.
 */
fun Int.toDuration(): Triple<Int, Int, Int> {
    val totalSeconds = this / 1000

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds - (hours * 3600)) / 60
    val seconds = totalSeconds % 60

    return Triple(hours, minutes, seconds)
}