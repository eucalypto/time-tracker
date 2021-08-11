package net.eucalypto.timetracker.activity.list

import android.app.TimePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.domain.model.Activity
import java.time.ZonedDateTime

class ActivityListViewModel(private val repository: Repository) : ViewModel() {

    val activityList: LiveData<List<Activity>> = repository.getActivitiesAsLiveData()


    lateinit var chosenActivity: Activity

    lateinit var timeToDisplay: ZonedDateTime
    var titleId: Int = 0
    lateinit var onTimeSet: TimePickerDialog.OnTimeSetListener

    private val _editTimeError = MutableLiveData<EditTimeError>(EditTimeError.NO_ERROR)
    val editTimeError: LiveData<EditTimeError> get() = _editTimeError

    fun delete(activity: Activity) = viewModelScope.launch {
        repository.delete(activity)
    }

    private fun update(activity: Activity) = viewModelScope.launch {
        repository.update(activity)
    }

    fun setNewEndTime(hourOfDay: Int, minute: Int) {
        val newEndTime = when {
            chosenActivity.isFinished() -> chosenActivity.endTime
            else -> ZonedDateTime.now()
        }.withHour(hourOfDay).withMinute(minute).withSecond(0)

        tryUpdateTime {
            update(chosenActivity.withEndTime(newEndTime))
        }
    }

    fun setNewStartTime(hourOfDay: Int, minute: Int) {
        val startTime =
            chosenActivity.startTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
        tryUpdateTime {
            update(chosenActivity.withStartTime(startTime))
        }
    }

    private fun tryUpdateTime(block: () -> Unit) {
        try {
            block()
        } catch (_: Activity.FutureTimeException) {
            _editTimeError.value = EditTimeError.FUTURE_TIME
        } catch (_: Activity.StartTimeBeforeEndTimeException) {
            _editTimeError.value = EditTimeError.START_AFTER_END
        }
    }

    fun resetEditTimeError() {
        _editTimeError.value = EditTimeError.NO_ERROR
    }
}

enum class EditTimeError {
    NO_ERROR, FUTURE_TIME, START_AFTER_END
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