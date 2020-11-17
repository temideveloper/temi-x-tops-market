package com.robosolutions.temixtopsmarket.extensions

import android.os.Bundle

enum class Keys {
    DURATION
}

//fun Bundle.putTemiLocation(location: String) = putString(Keys.TEMI_LOCATION.name, location)
//
//fun Bundle.getTemiLocation() = getString(Keys.TEMI_LOCATION.name)

fun Bundle.putDuration(duration: Int) = putInt(Keys.DURATION.name, duration)

fun Bundle.getDuration() = getInt(Keys.DURATION.name).run {
    if (this == 0) null else this
}