package net.eucalypto.timetracker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Category(
    val name: String = "",
    val id: UUID = UUID.randomUUID()
) : Parcelable {

    /**
     * Create new Category with new name but same id.
     */
    fun withName(newName: String): Category {
        return Category(newName, id)
    }
}