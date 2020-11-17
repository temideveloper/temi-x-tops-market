package com.robosolutions.temixtopsmarket.extensions

import android.os.Bundle

enum class Keys {
    DURATION, URL_STRING
}

fun Bundle.putUrl(url: String) = putString(Keys.URL_STRING.name, url)

fun Bundle.getUrl() = getString(Keys.URL_STRING.name)

fun Bundle.putDuration(duration: Int) = putInt(Keys.DURATION.name, duration)

fun Bundle.getDuration() = getInt(Keys.DURATION.name).run {
    if (this == 0) null else this
}