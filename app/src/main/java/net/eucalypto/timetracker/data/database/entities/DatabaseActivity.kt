package net.eucalypto.timetracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "activities")
data class DatabaseActivity(
    val categoryId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Long,
)