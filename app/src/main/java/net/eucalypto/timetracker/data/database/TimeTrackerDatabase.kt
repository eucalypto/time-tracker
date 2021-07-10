package net.eucalypto.timetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.eucalypto.timetracker.data.database.entities.DatabaseActivity
import net.eucalypto.timetracker.data.database.entities.DatabaseCategory
import net.eucalypto.timetracker.data.database.entities.RoomTypeConverters

@Database(entities = [DatabaseCategory::class, DatabaseActivity::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class TimeTrackerDatabase : RoomDatabase() {

    abstract val categoryDao: DatabaseCategoryDao
    abstract val activityDao: DatabaseActivityDao

}


@Volatile
private lateinit var INSTANCE: TimeTrackerDatabase

fun getDatabase(context: Context): TimeTrackerDatabase {
    synchronized(TimeTrackerDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TimeTrackerDatabase::class.java,
                "timetracker.db"
            ).build()
        }

        return INSTANCE
    }
}