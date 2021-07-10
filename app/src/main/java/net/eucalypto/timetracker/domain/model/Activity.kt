package net.eucalypto.timetracker.domain.model

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class Activity(
    val category: Category,
    val startTime: ZonedDateTime = ZonedDateTime.now(),
    var endTime: ZonedDateTime = NOT_SET_YET,
    val id: Long = 0,
) {
    fun isFinished(): Boolean {
        return endTime != NOT_SET_YET
    }
}

val NOT_SET_YET: ZonedDateTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())


fun ZonedDateTime.asCustomFormatString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm") // e.g. 2021-10-07 21:43
    return this.format(formatter)
}