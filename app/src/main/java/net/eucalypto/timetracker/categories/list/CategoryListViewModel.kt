package net.eucalypto.timetracker.categories.list

import androidx.lifecycle.ViewModel
import net.eucalypto.timetracker.data.model.Category
import java.util.*

class CategoryListViewModel : ViewModel() {

    val dummyList = listOf(
        Category(UUID.randomUUID(), "Category 1"),
        Category(UUID.randomUUID(), "Category 2"),
        Category(UUID.randomUUID(), "Category 3"),
        Category(UUID.randomUUID(), "Category 4"),
    )

}