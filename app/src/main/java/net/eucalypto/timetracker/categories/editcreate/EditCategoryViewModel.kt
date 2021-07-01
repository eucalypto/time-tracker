package net.eucalypto.timetracker.categories.editcreate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Category

class EditCategoryViewModel(private val repo: Repository) : ViewModel() {

    val categoryName = MutableLiveData<String>()


    fun saveCategory() = viewModelScope.launch {
        categoryName.value?.let {
            repo.insertCategory(Category(it))
        }
    }


    class Factory(private val repository: Repository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(EditCategoryViewModel::class.java)) {
                throw IllegalArgumentException("Unknown class: ${modelClass.name}")
            }

            return EditCategoryViewModel(repository) as T
        }

    }
}