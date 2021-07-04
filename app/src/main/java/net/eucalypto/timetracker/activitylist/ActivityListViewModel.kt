package net.eucalypto.timetracker.activitylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Activity

class ActivityListViewModel(repository: Repository) : ViewModel() {

    val activityList: LiveData<List<Activity>> = repository.getActivitiesAsLiveData()

}


class ActivityListViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(ActivityListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        return ActivityListViewModel(repository) as T
    }

}