package de.eucalypto.timetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [WorkStatus::class])
abstract class WorkStatusDatabase
    : RoomDatabase() {
}