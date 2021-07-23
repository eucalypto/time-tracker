package net.eucalypto.timetracker.activities

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.ActivityTimeException

class ActivityListFragment : Fragment() {

    private val viewModel: ActivityListViewModel by viewModels() {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

    private lateinit var binding: ActivityListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.activityList.adapter = ActivityAdapter(
            onDeleteClicked = ::showDeleteConfirmationDialog,
            onEditEndTimeClicked = ::showEndTimeChooserDialog,
            onEditStartTimeClicked = ::showStartTimeChooserDialog
        )
        binding.activityList.layoutManager = LinearLayoutManager(context)
    }

    private fun showStartTimeChooserDialog(activity: Activity) {
        StartTimePickerFragment(activity) { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val startTime = activity.startTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
            try {
                val updatedActivity = activity.withStartTime(startTime)
                viewModel.update(updatedActivity)
            } catch (_: ActivityTimeException) {
                showTimeExceptionSnackbar()
            }
        }.show(parentFragmentManager, "startTimePicker")
    }

    private fun showEndTimeChooserDialog(activity: Activity) {
        EndTimePickerFragment(activity) { view: TimePicker?, hourOfDay: Int, minute: Int ->
            val newEndTime = activity.endTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
            try {
                val updatedActivity = activity.withEndTime(newEndTime)
                viewModel.update(updatedActivity)
            } catch (_: ActivityTimeException) {
                showTimeExceptionSnackbar()
            }
        }.show(parentFragmentManager, "endTimePicker")
    }

    private fun showTimeExceptionSnackbar() {
        Snackbar
            .make(
                binding.root,
                R.string.activity_edit_dialog_end_time_before_start_time,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(R.string.ok_button) {}
            .show()
    }

    private fun showDeleteConfirmationDialog(activity: Activity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.activity_list_dialog_delete_title)
            .setMessage(R.string.activity_list_dialog_delete_message)
            .setPositiveButton(R.string.dialog_delete_button_delete) { _, _ ->
                viewModel.delete(activity)
            }
            .setNegativeButton(R.string.dialog_delete_button_cancel) { _, _ -> }
            .show()
    }

}

class EndTimePickerFragment(
    private val activityToEdit: Activity, private val onTimeSet: TimePickerDialog.OnTimeSetListener
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = activityToEdit.endTime.hour
        val minute = activityToEdit.endTime.minute
        val is24hoursView = true

        return TimePickerDialog(
            activity,
            onTimeSet,
            hour,
            minute,
            is24hoursView
        ).apply {
            setTitle(R.string.activity_edit_dialog_end_time_title)
        }
    }
}

class StartTimePickerFragment(
    private val activityToEdit: Activity,
    private val onTimeSet: TimePickerDialog.OnTimeSetListener
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = activityToEdit.startTime.hour
        val minute = activityToEdit.startTime.minute
        val is24hoursView = true

        return TimePickerDialog(
            activity,
            onTimeSet,
            hour,
            minute,
            is24hoursView
        ).apply {
            setTitle(R.string.activity_edit_dialog_start_time_title)
        }
    }
}