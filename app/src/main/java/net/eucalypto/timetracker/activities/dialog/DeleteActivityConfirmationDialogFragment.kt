package net.eucalypto.timetracker.activities.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import net.eucalypto.timetracker.R

class DeleteActivityConfirmationDialogFragment : DialogFragment() {

    private val viewModel: DialogViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.activity_list_dialog_delete_title)
            .setMessage(R.string.activity_list_dialog_delete_message)
            .setPositiveButton(R.string.dialog_delete_button_delete, viewModel.onDeleteConfirmation)
            .setNegativeButton(R.string.dialog_delete_button_cancel) { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "DeleteActivityConfirmationDialogFragment"
    }
}