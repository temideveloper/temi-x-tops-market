package com.robosolutions.temixtopsmarket.extensions

import com.robotemi.sdk.UserInfo

fun UserInfo.isSameAs(other: UserInfo) =
    userId == other.userId && name == other.name && picUrl == other.picUrl && role == other.role