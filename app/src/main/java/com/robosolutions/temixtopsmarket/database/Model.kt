package com.robosolutions.temixtopsmarket.database

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.File
import java.util.*

@Entity
data class ContactableStaff(@PrimaryKey val userId: String)

@Entity
data class PromotionVideo(
    val title: String,
    val summary: String,
    @PrimaryKey val id: UUID = UUID.randomUUID()
) {
    fun getVideoFile(context: Context): File {
        val parentDirectory =
            File(context.applicationContext.filesDir, VideoRepository.VIDEO_FOLDER).also {
                it.mkdirs()
            }

        return File(parentDirectory, id.toString())
    }
}

object Converter {
    @TypeConverter
    fun fromUuid(id: UUID) = id.toString()

    @TypeConverter
    fun toUuid(id: String): UUID = UUID.fromString(id)
}