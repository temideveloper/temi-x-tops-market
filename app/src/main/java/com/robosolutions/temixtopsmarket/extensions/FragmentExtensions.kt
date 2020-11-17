package com.robosolutions.temixtopsmarket.extensions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavDirections
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Fetch data from fragment result by [NavDirections].
 *
 * @param T The data type.
 * @param view The [View] that initiates the call.
 * @param requestKey The request key that identifies the fragment result request.
 * @param dir The direction that gives result.
 * @param extractBlock Provides the logic to extract the data from the bundle.
 */
suspend fun <T> Fragment.getByFragmentResult(
    view: View,
    requestKey: String,
    dir: NavDirections,
    extractBlock: (Bundle) -> T
) = suspendCoroutine<T> {
    setFragmentResultListener(requestKey) { _, bundle ->
        it.resume(extractBlock(bundle))
    }

    view.navigate(dir)
}