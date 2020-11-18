package com.robosolutions.temixtopsmarket.extensions

import com.robotemi.sdk.Robot
import com.robotemi.sdk.UserInfo

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

