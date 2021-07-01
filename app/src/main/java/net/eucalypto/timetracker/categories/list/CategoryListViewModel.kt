package net.eucalypto.timetracker.categories.list

import androidx.lifecycle.ViewModel
import net.eucalypto.timetracker.domain.model.Category

class CategoryListViewModel : ViewModel() {

    val dummyList = listOf(
        Category("Category 1"),
        Category("Category 2"),
        Category("Category 3"),
        Category("Category 4"),
    )

}