package net.eucalypto.timetracker.data.database.entities

import androidx.room.TypeConverter
import java.time.ZonedDateTime
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
    fun fromLocalDateTime(dateTime: ZonedDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): ZonedDateTime {
        return ZonedDateTime.parse(dateTimeString)
    }
}