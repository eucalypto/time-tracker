package net.eucalypto.timetracker.data.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*


class RoomTypeConverters {

    @TypeConverter
    fun toUUID(string: String): UUID {
        return UUID.fromString(string)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString)
    }
}