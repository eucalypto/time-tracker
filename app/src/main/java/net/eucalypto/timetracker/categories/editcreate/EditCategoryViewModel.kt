package net.eucalypto.timetracker.categories.editcreate

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Category

class EditCategoryViewModel(private val repo: Repository) : ViewModel() {

    val categoryName = MutableLiveData<String>()

    val isFinished: LiveData<Boolean>
        get() = _isFinished
    private val _isFinished = MutableLiveData(false)

    fun onDoneButtonClicked() {
        viewModelScope.launch { saveCategory() }
        _isFinished.value = true
    }

    private suspend fun saveCategory() {
        categoryName.value?.let {
            if (it.isEmpty()) return@let
            repo.insertCategory(Category(it))
        }
    }


    class Factory(private val repository: Repository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(EditCategoryViewModel::class.java)) {
                throw IllegalArgumentException("Unknown class: ${modelClass.name}")
            }

            @Suppress("unchecked_cast")
            return EditCategoryViewModel(repository) as T
        }

    }
}