package net.eucalypto.timetracker.category.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Category

class CategoryListViewModel(private val repository: Repository) : ViewModel() {

    val categoryList: LiveData<List<Category>> = repository.getCategoriesAsLiveData()

    lateinit var categoryToDelete: Category

    fun deleteCategoryAndCorrespondingActivities(category: Category) {
        viewModelScope.launch {
            val correspondingActivities = repository.getActivitiesWithCategory(category)
            repository.delete(correspondingActivities)
            repository.delete(category)
        }
    }

    fun saveCategory(category: Category) = viewModelScope.launch {
        repository.update(category)
    }

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