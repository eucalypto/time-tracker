package net.eucalypto.timetracker.data.database

import androidx.room.TypeConverter
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
}