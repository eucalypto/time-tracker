package net.eucalypto.timetracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.eucalypto.timetracker.domain.model.Category
import java.util.*

@Entity(tableName = "activity_categories")
data class DatabaseCategory(
    @PrimaryKey
    val id: UUID,
    val name: String
)

fun Category.asDatabaseModel(): DatabaseCategory {
    return DatabaseCategory(id, name)
}

fun List<DatabaseCategory>.asDomainModel(): List<Category> = this.map {
    Category(it.name, it.id)
}