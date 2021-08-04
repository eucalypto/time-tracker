package net.eucalypto.timetracker.activity.list

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.activity.list.dialog.DeleteActivityConfirmationDialogFragment
import net.eucalypto.timetracker.activity.list.dialog.DialogViewModel
import net.eucalypto.timetracker.activity.list.dialog.TimePickerDialogFragment
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.ActivityFutureTimeException
import net.eucalypto.timetracker.domain.model.ActivityTimeLineException

class ActivityListFragment : Fragment() {

    private val viewModel: ActivityListViewModel by viewModels {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

    private val dialogViewModel: DialogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActivityListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val binding = DataBindingUtil.getBinding<ActivityListFragmentBinding>(view)!!

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setup(binding.activityList)
    }

    private fun setup(activityList: RecyclerView) {
        activityList.adapter = ActivityAdapter(
            ActivityPopupMenuCallbacks(
                onDeleteClicked = ::showDeleteConfirmationDialog,
                onEditEndTimeClicked = ::showEndTimeChooserDialog,
                onEditStartTimeClicked = ::showStartTimeChooserDialog
            )
        )
        activityList.layoutManager = LinearLayoutManager(context)
    }

    private fun showDeleteConfirmationDialog(activity: Activity) {
        dialogViewModel.onDeleteConfirmation = DialogInterface.OnClickListener { _, _ ->
            viewModel.delete(activity)
        }
        DeleteActivityConfirmationDialogFragment().show(
            childFragmentManager,
            DeleteActivityConfirmationDialogFragment.TAG
        )
    }

    private fun showStartTimeChooserDialog(activity: Activity) {
        dialogViewModel.apply {
            timeToDisplay = activity.startTime
            titleId = R.string.activity_edit_dialog_start_time_title
            onTimeSet =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    val startTime =
                        activity.startTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
                    tryUpdateActivity { activity.withStartTime(startTime) }
                }
        }

        TimePickerDialogFragment().show(parentFragmentManager, "startTimePicker")
    }

    private fun showEndTimeChooserDialog(activity: Activity) {
        dialogViewModel.apply {
            timeToDisplay = activity.endTime
            titleId = R.string.activity_edit_dialog_end_time_title
            onTimeSet = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val newEndTime =
                    activity.endTime.withHour(hourOfDay).withMinute(minute).withSecond(0)
                tryUpdateActivity { activity.withEndTime(newEndTime) }
            }
        }

        TimePickerDialogFragment().show(parentFragmentManager, "endTimePicker")
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
                requireView(),
                resId,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(R.string.ok_button) {}
            .show()
    }
}