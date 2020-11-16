package com.robosolutions.temixtopsmarket.extensions

/**
 * Returns `true` if the [String] contains both a lowercase character and an uppercase character.
 *
 */
fun String.isMixedCase() = contains(Regex("[A-Z]")) && contains(Regex("[a-z]"))