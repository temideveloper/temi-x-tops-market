package com.robosolutions.temixtopsmarket.extensions

import android.app.Activity
import android.content.pm.PackageManager
import com.robotemi.sdk.Robot
import com.robotemi.sdk.UserInfo
import timber.log.Timber

/**
 * Returns a [Robot] instance.
 */
val robot
    get() = Robot.getInstance()

/**
 * Returns all temi robot's contact and also the admin if any.
 */
val Robot.contactsAndAdmin
    get() = mutableListOf<UserInfo>().apply {
        addAll(allContact)
        adminInfo?.let { add(it) }
    }.toList()

/**
 * Returns alphabetically sorted temi robot's contact and admin.
 */
val Robot.contactsAndAdminSorted
    get() = contactsAndAdmin.sortedBy { it.name }

/**
 * Hides temi's top bar completely.
 *
 * @param activity The activity that contains the UI MODE metadata.
 */
fun Robot.completeHideTopBar(activity: Activity) = try {
    val packageManager = activity.packageManager
    val componentName = activity.componentName

    val activityInfo =
        packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)

    onStart(activityInfo)
} catch (e: PackageManager.NameNotFoundException) {
    Timber.e(e, "Error on hiding temi's top bar")
}

