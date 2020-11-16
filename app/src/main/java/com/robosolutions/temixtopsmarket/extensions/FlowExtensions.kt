package com.robosolutions.temixtopsmarket.extensions

import kotlinx.coroutines.flow.MutableStateFlow

infix fun <T> MutableStateFlow<T>.updateTo(newValue: T) {
    value = newValue
}