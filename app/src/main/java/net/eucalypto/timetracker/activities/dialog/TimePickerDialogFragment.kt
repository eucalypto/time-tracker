package net.eucalypto.timetracker.activities.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels


class TimePickerDialogFragment : DialogFragment() {

    val viewModel: DialogViewModel by activityViewModels()

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