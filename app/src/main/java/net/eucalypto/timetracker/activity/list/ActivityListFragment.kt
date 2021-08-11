package net.eucalypto.timetracker.activity.list

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.activity.list.dialog.DeleteActivityConfirmationDialogFragment
import net.eucalypto.timetracker.activity.list.dialog.TimePickerDialogFragment
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding
import net.eucalypto.timetracker.domain.model.Activity
import java.time.ZonedDateTime

class ActivityListFragment : Fragment() {

    private val viewModel: ActivityListViewModel by activityViewModels {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

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

        viewModel.editTimeError.observe(viewLifecycleOwner) { editTimeError ->
            showErrorSnackbar(editTimeError)
        }
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

    private fun showErrorSnackbar(editTimeError: EditTimeError?) {
        when (editTimeError) {
            EditTimeError.FUTURE_TIME -> {
                showSnackbarWithText(R.string.activity_edit_dialog_start_or_end_time_in_the_future)
            }
            EditTimeError.START_AFTER_END -> {
                showSnackbarWithText(R.string.activity_edit_dialog_end_time_before_start_time)
            }
            else -> {
                return
            }
        }
        viewModel.resetEditTimeError()
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

    private fun showDeleteConfirmationDialog(activity: Activity) {
        viewModel.chosenActivity = activity
        DeleteActivityConfirmationDialogFragment().show(
            childFragmentManager,
            DeleteActivityConfirmationDialogFragment.TAG
        )
    }

    private fun showStartTimeChooserDialog(activity: Activity) {
        viewModel.apply {
            chosenActivity = activity
            timeToDisplay = activity.startTime
            titleId = R.string.activity_edit_dialog_start_time_title
            onTimeSet =
                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    viewModel.setNewStartTime(hourOfDay, minute)
                }
        }
        TimePickerDialogFragment().show(
            parentFragmentManager,
            TimePickerDialogFragment.TAG_START_TIME
        )
    }

    private fun showEndTimeChooserDialog(activity: Activity) {
        viewModel.apply {
            chosenActivity = activity
            timeToDisplay = if (activity.isFinished()) activity.endTime else ZonedDateTime.now()
            titleId = R.string.activity_edit_dialog_end_time_title
            onTimeSet = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                viewModel.setNewEndTime(hourOfDay, minute)
            }
        }
        TimePickerDialogFragment().show(
            parentFragmentManager,
            TimePickerDialogFragment.TAG_END_TIME
        )
    }
}