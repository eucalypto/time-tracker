package net.eucalypto.timetracker.domain.model

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


data class Activity(
    val category: Category,
    val startTime: ZonedDateTime = ZonedDateTime.now(),
    val endTime: ZonedDateTime = NOT_SET_YET,
    val id: Long = 0,
) {
    init {
        throwExceptionIfDataCorrupt()
    }

    private fun throwExceptionIfDataCorrupt() {
        if (endTime != NOT_SET_YET && endTime < startTime) throw StartTimeBeforeEndTimeException()

        val now = ZonedDateTime.now()
        if (startTime > now || endTime > now) throw FutureTimeException()
    }

    val duration: Duration
        get() {
            if (!isFinished()) {
                return Duration.ZERO
            }
            return Duration.between(startTime, endTime)
        }

    fun isFinished(): Boolean {
        return endTime != NOT_SET_YET
    }

    fun withEndTime(endTime: ZonedDateTime): Activity {
        return Activity(category, startTime, endTime, id)
    }

    fun withStartTime(startTime: ZonedDateTime): Activity {
        return Activity(category, startTime, endTime, id)
    }

    class StartTimeBeforeEndTimeException : IllegalArgumentException()
    class FutureTimeException : IllegalArgumentException()
}

val NOT_SET_YET: ZonedDateTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())

