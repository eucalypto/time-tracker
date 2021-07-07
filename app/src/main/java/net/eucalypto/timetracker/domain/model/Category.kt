package net.eucalypto.timetracker.domain.model

import java.util.*

data class Category(
    val name: String = "",
    val id: UUID = UUID.randomUUID()
) {

    /**
     * Create new Category with new name but same id.
     */
    fun withName(newName: String): Category {
        return Category(newName, id)
    }
}
