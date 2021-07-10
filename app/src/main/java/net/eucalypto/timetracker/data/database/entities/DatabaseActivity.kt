package net.eucalypto.timetracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "activities")
data class DatabaseActivity(
    val categoryId: String,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Long,
)