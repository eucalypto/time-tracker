package net.eucalypto.timetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [WorkStatus::class])
abstract class WorkStatusDatabase
    : RoomDatabase() {

    abstract val workStatusDatabaseDao: WorkStatusDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: WorkStatusDatabase? = null

        fun getInstance(applicationContext: Context): WorkStatusDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        applicationContext,
                        WorkStatusDatabase::class.java,
                        "work_status_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}