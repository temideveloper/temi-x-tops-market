package com.robosolutions.temixtopsmarket.extensions

import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator

/**
 * Navigates to another destination.
 *
 * @param resId The destination id.
 */
fun View?.navigate(resId: Int) = this?.findNavController()?.navigate(resId)

/**
 * Navigates to another destination by [NavDirections].
 *
 * @param dir The destination direction.
 */
fun View?.navigate(dir: NavDirections) = this?.findNavController()?.navigate(dir)

/**
 * Navigates to another destination by [NavDirections] with extras.
 *
 * @param dir The destination direction.
 * @param extras Navigation extras.
 */
fun View?.navigate(dir: NavDirections, extras: FragmentNavigator.Extras) =
    this?.findNavController()?.navigate(dir, extras)


/**
 * When the [View] is clicked, the application will navigate based on the given action id.
 *
 * @param resId The action id.
 */
@BindingAdapter("onClickNavigate")
fun View?.navigateOnClick(resId: Int) =
    this?.setOnClickListener { findNavController().navigate(resId) }

/**
 * Sets the [isVisible] property of the [View].
 *
 * @param isVisible The new value for the [isVisible] property.
 */
@BindingAdapter("isVisible")
fun View.dynamicVisibility(isVisible: Boolean?) {
    this.isVisible = isVisible ?: false
}

/**
 * Sets the visibility of the [View]. Using this adapter, the [View] will go to [View.INVISIBLE]
 * instead of [View.GONE].
 *
 * @param isVisible The new visibility.
 */
@BindingAdapter("canVisible")
fun View.dynamicInvisibleVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

/**
 * Adds an action when the global layout has been calculated.
 *
 * @param block The action to do.
 */
inline fun View.doOnGlobalLayout(crossinline block: (View) -> Unit) {
    val treeObserver = viewTreeObserver

    treeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

            block(this@doOnGlobalLayout)
        }
    })
}