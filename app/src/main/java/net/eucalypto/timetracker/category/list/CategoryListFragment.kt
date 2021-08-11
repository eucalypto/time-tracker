package net.eucalypto.timetracker.category.list

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.CategoryEditNameDialogBinding
import net.eucalypto.timetracker.databinding.CategoryListFragmentBinding
import net.eucalypto.timetracker.domain.model.Category
import net.eucalypto.timetracker.domain.model.util.asParcel

class CategoryListFragment : Fragment() {

    private val viewModel: CategoryListViewModel by activityViewModels {
        CategoryListViewModel.Factory(getRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CategoryListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.getBinding<CategoryListFragmentBinding>(view)!!

        setupRecyclerView(binding.categoryList)
        setupAddCategoryFab(binding)
    }

    private fun setupAddCategoryFab(binding: CategoryListFragmentBinding) {
        binding.addCategoryFab.setOnClickListener {
            val toCreateCategory =
                CategoryListFragmentDirections
                    .actionToCreateCategoryFragment(Category().asParcel())
            findNavController().navigate(toCreateCategory)
        }
    }

    private fun setupRecyclerView(categoryList: RecyclerView) {
        val categoryAdapter = CategoryAdapter(
            CategoryPopupMenuCallbacks(
                onWriteNfcClicked = ::showWriteNfcActivityAsDialog,
                onEditClicked = ::showEditCategoryDialog,
                onDeleteClicked = ::showDeleteConfirmationDialog
            )
        )
        categoryList.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showWriteNfcActivityAsDialog(category: Category) {
        val toWriteNfc =
            CategoryListFragmentDirections.actionToWriteNfcActivity(category.asParcel())
        findNavController().navigate(toWriteNfc)
    }

    private fun showEditCategoryDialog(category: Category) {
        viewModel.categoryToUpdate = category
        val toEditCategory =
            CategoryListFragmentDirections.actionToEditCategoryDialogFragment()
        findNavController().navigate(toEditCategory)
    }

    private fun showDeleteConfirmationDialog(category: Category) {
        viewModel.categoryToDelete = category
        val toDeleteConfirmationDialog =
            CategoryListFragmentDirections.actionToDeleteCategoryConfirmationDialogFragment()
        findNavController().navigate(toDeleteConfirmationDialog)
    }
}


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