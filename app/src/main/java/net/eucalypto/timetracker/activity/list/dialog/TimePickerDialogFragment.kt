package net.eucalypto.timetracker.activity.list.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.activity.list.ActivityListViewModel
import net.eucalypto.timetracker.activity.list.ActivityListViewModelFactory
import net.eucalypto.timetracker.activity.list.StartOrEndTime
import net.eucalypto.timetracker.data.getRepository


class TimePickerDialogFragment : DialogFragment() {

    val viewModel: ActivityListViewModel by activityViewModels() {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = viewModel.timeToDisplay.hour
        val minute = viewModel.timeToDisplay.minute
        val is24hoursView = true

        return TimePickerDialog(
            activity,
            { _, hourOfDay, minute ->
                when (viewModel.startOrEndTime) {
                    StartOrEndTime.START -> viewModel.setNewStartTime(hourOfDay, minute)
                    StartOrEndTime.END -> viewModel.setNewEndTime(hourOfDay, minute)
                }
            },
            hour,
            minute,
            is24hoursView
        ).apply {
            setTitle(
                when (viewModel.startOrEndTime) {
                    StartOrEndTime.START -> R.string.activity_edit_dialog_start_time_title
                    StartOrEndTime.END -> R.string.activity_edit_dialog_end_time_title
                }
            )
        }
    }


    companion object {
        const val TAG_END_TIME = "endTimePicker"
        const val TAG_START_TIME = "startTimePicker"
    }
}