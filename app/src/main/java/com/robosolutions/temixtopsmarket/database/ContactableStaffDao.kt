package com.robosolutions.temixtopsmarket.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactableStaffDao {
    @Query("SELECT * FROM ContactableStaff")
    fun getStaffs(): Flow<List<ContactableStaff>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: ContactableStaff)

    @Query("DELETE FROM ContactableStaff WHERE userId = :staffUserId")
    suspend fun removeStaff(staffUserId: String)
}