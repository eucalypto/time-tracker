package net.eucalypto.timetracker.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Activity

class ActivityListViewModel(private val repository: Repository) : ViewModel() {

    val activityList: LiveData<List<Activity>> = repository.getActivitiesAsLiveData()

    fun delete(activity: Activity) = viewModelScope.launch {
        repository.delete(activity)
    }
}


class ActivityListViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(ActivityListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        @Suppress("unchecked_cast")
        return ActivityListViewModel(repository) as T
    }

}