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
        val endTimeIsSetAndBeforeStartTime = endTime != NOT_SET_YET && endTime < startTime
        if (endTimeIsSetAndBeforeStartTime) throw ActivityTimeLineException()

        val now = ZonedDateTime.now()
        val startTimeOrEndTimeInTheFuture = startTime > now || endTime > now
        if (startTimeOrEndTimeInTheFuture) throw ActivityFutureTimeException()
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
}

val NOT_SET_YET: ZonedDateTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())

class ActivityTimeLineException : IllegalArgumentException()
class ActivityFutureTimeException : IllegalArgumentException()