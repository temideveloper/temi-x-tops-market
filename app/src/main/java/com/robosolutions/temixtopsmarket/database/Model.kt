package com.robosolutions.temixtopsmarket.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactableStaff(@PrimaryKey val userId: String)

@Entity
data class PromotionVideo(
    val title: String,
    val summary: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)