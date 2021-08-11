package net.eucalypto.timetracker.activity.list

import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Activity
import java.time.ZonedDateTime

class ActivityListViewModel(private val repository: Repository) : ViewModel() {

    val activityList: LiveData<List<Activity>> = repository.getActivitiesAsLiveData()


    lateinit var onDeleteConfirmation: DialogInterface.OnClickListener

    lateinit var timeToDisplay: ZonedDateTime
    var titleId: Int = 0
    lateinit var onTimeSet: TimePickerDialog.OnTimeSetListener


    fun delete(activity: Activity) = viewModelScope.launch {
        repository.delete(activity)
    }

    fun update(activity: Activity) = viewModelScope.launch {
        repository.update(activity)
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