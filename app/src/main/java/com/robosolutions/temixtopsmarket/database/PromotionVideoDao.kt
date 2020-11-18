package com.robosolutions.temixtopsmarket.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PromotionVideoDao {
    @Query("SELECT * FROM PromotionVideo")
    fun getVideos(): Flow<List<PromotionVideo>>

    @Insert
    fun saveVideo(video: PromotionVideo)

    @Delete
    fun deleteVideo(video: PromotionVideo)
}