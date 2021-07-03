package net.eucalypto.timetracker.domain.model.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.eucalypto.timetracker.domain.model.Category
import java.util.*


@Parcelize
data class CategoryParcel(
    val name: String,
    val id: UUID
) : Parcelable

fun Category.asParcel(): CategoryParcel {
    return CategoryParcel(name, id)
}

fun CategoryParcel.asCategory(): Category {
    return Category(name, id)
}