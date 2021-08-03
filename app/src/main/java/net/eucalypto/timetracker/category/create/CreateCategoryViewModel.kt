package net.eucalypto.timetracker.category.create

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Category
import java.util.*

class CreateCategoryViewModel(
    private val repo: Repository,
    private val category: Category
) : ViewModel() {

    val categoryName = MutableLiveData(category.name)

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
            repo.insert(category.withName(it))
        }
    }


    class Factory(private val repository: Repository, private val category: Category) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
                throw IllegalArgumentException("Unknown class: ${modelClass.name}")
            }

            @Suppress("unchecked_cast")
            return CreateCategoryViewModel(repository, category) as T
        }

    }
}