package net.eucalypto.timetracker.ui.activity.list.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.ui.activity.list.ActivityListViewModel
import net.eucalypto.timetracker.ui.activity.list.ActivityListViewModelFactory

class DeleteActivityConfirmationDialogFragment : DialogFragment() {

    private val viewModel: ActivityListViewModel by activityViewModels() {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.activity_list_dialog_delete_title)
            .setMessage(R.string.activity_list_dialog_delete_message)
            .setPositiveButton(R.string.dialog_delete_button_delete) { _, _ ->
                viewModel.delete(viewModel.chosenActivity)
            }
            .setNegativeButton(R.string.dialog_delete_button_cancel) { _, _ -> }
            .create()
    }
}