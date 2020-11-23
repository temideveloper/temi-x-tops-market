package com.robosolutions.temixtopsmarket.extensions

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

infix fun <T> MutableStateFlow<T>.updateTo(newValue: T) {
    value = newValue
}

fun MutableStateFlow<Int>.increment() {
    val currentValue = value
    value = currentValue + 1
}

fun MutableStateFlow<Int>.decrement() {
    val currentValue = value
    value = currentValue - 1
}

/**
 * Creates a timer-like flow. Call the [collect] method to start the timer.
 *
 * @param seconds The duration in seconds.
 * @param onElapse The action to do when a second passed.
 * @param onTimesUp The action to do when the timer is up.
 */
fun timer(seconds: Int, onElapse: (Int) -> Unit = {}, onTimesUp: (Int) -> Unit = {}) =
    (seconds downTo 1)
        .asFlow()
        .onEach {
            onElapse(it)
            delay(1000)
        }.onCompletion {
            onTimesUp(seconds)
        }.cancellable()

/**
 * Fetch 1 value from the flow and return it.
 *
 */
suspend fun <T> Flow<T>.singleLatest() = take(1).single()

/**
 * Combines both [Flow] object and check if both content is equal and both must not be blank.
 *
 * @param secondFlow The second [Flow] object.
 */
fun <T : CharSequence> Flow<T>.combineAndCheckEqualNotBlank(secondFlow: Flow<T>) =
    combine(secondFlow) { first, second -> first == second && first.isNotBlank() && second.isNotBlank() }

/**
 * Converts integer flow into a [String] flow where the [String] is retrieved from the resource.
 *
 * @param context The context.
 */
fun Flow<Int>.mapStringResource(context: Context) = map { context.getString(it) }

/**
 * Combines both [Flow] object into a flow of [Pair].
 *
 * @param secondFlow The second [Flow] object.
 */
fun <A, B> Flow<A>.combineToPair(secondFlow: Flow<B>) = combine(secondFlow) { first, second ->
    Pair(first, second)
}

/**
 * Combines both [Flow] object into a flow of [Triple].
 *
 * @param secondFlow The second [Flow] object.
 * @param thirdFlow The third [Flow] object.
 */
fun <A, B, C> Flow<A>.combineToTriple(secondFlow: Flow<B>, thirdFlow: Flow<C>) =
    combineToPair(secondFlow)
        .combine(thirdFlow) { (first, second), third -> Triple(first, second, third) }
