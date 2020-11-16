package com.robosolutions.temixtopsmarket.extensions

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

infix fun <T> MutableStateFlow<T>.updateTo(newValue: T) {
    value = newValue
}

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
