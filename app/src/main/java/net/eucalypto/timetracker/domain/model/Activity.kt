package net.eucalypto.timetracker.domain.model

import java.time.LocalDateTime


data class Activity(
    val category: Category,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = NOT_SET_YET,
    val id: Long = 0,
)

val NOT_SET_YET: LocalDateTime = LocalDateTime.MIN