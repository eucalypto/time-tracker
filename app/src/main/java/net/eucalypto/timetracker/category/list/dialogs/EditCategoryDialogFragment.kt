package net.eucalypto.timetracker.category.list.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.category.list.CategoryListViewModel
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.CategoryEditNameDialogBinding

class EditCategoryDialogFragment : DialogFragment() {

    private val viewModel: CategoryListViewModel by activityViewModels {
        CategoryListViewModel.Factory(getRepository(requireContext()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inputBinding = CategoryEditNameDialogBinding.inflate(requireActivity().layoutInflater)
        val category = viewModel.categoryToUpdate
        inputBinding.categoryNameEdit.editText?.setText(category.name)

        return AlertDialog.Builder(requireContext())
            .setView(inputBinding.root)
            .setTitle(getString(R.string.edit_category_name_dialog_title))
            .setMessage(
                getString(
                    R.string.edit_category_name_dialog_message,
                    category.name
                )
            )
            .setNegativeButton(
                getString(R.string.edit_category_name_dialog_button_cancel),
                null
            )
            .setPositiveButton(
                getString(R.string.edit_category_name_dialog_button_rename)
            ) { _, _ ->
                inputBinding.categoryNameEdit.editText?.let {
                    val input = it.text.toString()
                    if (input.isBlank() || input == category.name) return@let
                    viewModel.saveCategory(category.withName(input))
                }
            }
            .create()
    }
}