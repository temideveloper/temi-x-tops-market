package com.robosolutions.temixtopsmarket.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ContactableStaff::class, PromotionVideo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val staffDao: ContactableStaffDao

    abstract val videoDao: PromotionVideoDao

    companion object {
        private const val DB_NAME = "AppDatabase"
        private lateinit var INSTANCE: AppDatabase

        fun create(context: Context) = synchronized(this) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
            }

            INSTANCE
        }
    }
}