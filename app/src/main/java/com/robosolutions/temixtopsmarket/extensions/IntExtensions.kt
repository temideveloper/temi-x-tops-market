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

/**
 * Returns this value only if this value is not zero. Otherwise, return the fallback value.
 *
 * @param other The fallback value. It must not be zero.
 * @return A non-zero value.
 */
infix fun Int.nonZeroOr(other: Int): Int {
    require(other != 0)

    return if (this == 0) other else this
}