package net.eucalypto.timetracker.activitylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Activity

class ActivityListViewModel(repository: Repository) : ViewModel() {

    private val haveCategoriesChanged: LiveData<Unit> = repository.getCategoriesAsLiveData().map { }

    val activityList: LiveData<List<Activity>> = haveCategoriesChanged.switchMap {
        repository.getActivitiesAsLiveData()
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