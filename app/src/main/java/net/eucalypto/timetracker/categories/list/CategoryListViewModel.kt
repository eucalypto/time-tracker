package net.eucalypto.timetracker.categories.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Category

class CategoryListViewModel(repository: Repository) : ViewModel() {

    val categoryList: LiveData<List<Category>> = repository.getCategoriesAsLiveData()


    class Factory(private val repository: Repository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(CategoryListViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }

            @Suppress("unchecked_cast")
            return CategoryListViewModel(repository) as T
        }

    }

}