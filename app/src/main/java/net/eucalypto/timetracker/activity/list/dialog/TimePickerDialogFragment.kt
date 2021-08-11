package net.eucalypto.timetracker.activity.list.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import net.eucalypto.timetracker.activity.list.ActivityListViewModel
import net.eucalypto.timetracker.activity.list.ActivityListViewModelFactory
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
            viewModel.onTimeSet,
            hour,
            minute,
            is24hoursView
        ).apply {
            setTitle(viewModel.titleId)
        }
    }
}