package com.robosolutions.temixtopsmarket.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactableStaffDao {
    @Query("SELECT * FROM ContactableStaff")
    fun getStaffs(): Flow<List<ContactableStaff>>

    @Insert
    suspend fun insertStaff(staff: ContactableStaff)

    @Delete
    suspend fun removeStaff(staff: ContactableStaff)
}