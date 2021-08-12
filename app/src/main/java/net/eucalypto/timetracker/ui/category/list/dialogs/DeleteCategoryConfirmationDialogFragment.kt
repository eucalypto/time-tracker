package net.eucalypto.timetracker.ui.category.list.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.ui.category.list.CategoryListViewModel

class DeleteCategoryConfirmationDialogFragment : DialogFragment() {

    val viewModel: CategoryListViewModel by activityViewModels() {
        CategoryListViewModel.Factory(getRepository(requireContext()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val category = viewModel.categoryToDelete
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_title, category.name))
            .setMessage(R.string.dialog_delete_message)
            .setNegativeButton(R.string.dialog_delete_button_cancel, null)
            .setPositiveButton(R.string.dialog_delete_button_delete) { _, _ ->
                viewModel.deleteCategoryAndCorrespondingActivities(category)
            }
            .create()
    }
}