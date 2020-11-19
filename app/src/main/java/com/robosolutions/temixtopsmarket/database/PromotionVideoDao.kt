package com.robosolutions.temixtopsmarket.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PromotionVideoDao {
    @Query("SELECT * FROM PromotionVideo ORDER BY title")
    fun getVideos(): Flow<List<PromotionVideo>>

    @Query("SELECT * FROM PromotionVideo WHERE id = :videoId")
    fun getVideoById(videoId: UUID): Flow<PromotionVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVideo(video: PromotionVideo)

    @Delete
    suspend fun deleteVideo(video: PromotionVideo)
}