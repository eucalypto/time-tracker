package net.eucalypto.timetracker.domain.model

import java.util.*

data class Category(
    val name: String,
    val id: UUID = UUID.randomUUID()
)