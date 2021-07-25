package net.eucalypto.timetracker.activities

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.ActivityFutureTimeException
import net.eucalypto.timetracker.domain.model.ActivityTimeLineException
import java.time.ZonedDateTime

class ActivityListFragment : Fragment() {

    private val viewModel: ActivityListViewModel by activityViewModels() {
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
        TimePickerFragment(
            activity.startTime,
            R.string.activity_edit_dialog_start_time_title
        ) { _: TimePicker?, hourOfDay: Int, minute: Int ->

            val startTime = activity.startTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
            tryUpdateActivity { activity.withStartTime(startTime) }

        }.show(parentFragmentManager, "startTimePicker")
    }

    private fun showEndTimeChooserDialog(activity: Activity) {
        TimePickerFragment(
            activity.endTime,
            R.string.activity_edit_dialog_end_time_title
        ) { view: TimePicker?, hourOfDay: Int, minute: Int ->

            val newEndTime = activity.endTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
            tryUpdateActivity { activity.withEndTime(newEndTime) }

        }.show(parentFragmentManager, "endTimePicker")
    }

    private fun tryUpdateActivity(getActivity: () -> Activity) {
        try {
            val updatedActivity = getActivity()
            viewModel.update(updatedActivity)
        } catch (_: ActivityTimeLineException) {
            showTimeLineExceptionSnackbar()
        } catch (_: ActivityFutureTimeException) {
            showFutureTimeExceptionSnackbar()
        }
    }

    private fun showFutureTimeExceptionSnackbar() {
        showSnackbarWithText(R.string.activity_edit_dialog_start_or_end_time_in_the_future)
    }

    private fun showTimeLineExceptionSnackbar() {
        showSnackbarWithText(R.string.activity_edit_dialog_end_time_before_start_time)
    }

    private fun showSnackbarWithText(resId: Int) {
        Snackbar
            .make(
                binding.root,
                resId,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(R.string.ok_button) {}
            .show()
    }

    private fun showDeleteConfirmationDialog(activity: Activity) {
        viewModel.selectedActivity = activity
        DeleteConfirmationDialogFragment().show(parentFragmentManager, "deleteConfirmationDialog")
    }

}

class TimePickerFragment(
    private val timeToDisplay: ZonedDateTime,
    private val titleId: Int,
    private val onTimeSet: TimePickerDialog.OnTimeSetListener
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = timeToDisplay.hour
        val minute = timeToDisplay.minute
        val is24hoursView = true

        return TimePickerDialog(
            activity,
            onTimeSet,
            hour,
            minute,
            is24hoursView
        ).apply {
            setTitle(titleId)
        }
    }
}