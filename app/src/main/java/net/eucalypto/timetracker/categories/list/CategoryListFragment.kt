package net.eucalypto.timetracker.categories.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.CategoryEditNameDialogBinding
import net.eucalypto.timetracker.databinding.CategoryListFragmentBinding
import net.eucalypto.timetracker.domain.model.Category
import net.eucalypto.timetracker.domain.model.util.asParcel

class CategoryListFragment : Fragment() {

    private val viewModel: CategoryListViewModel by viewModels {
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

        setupCategoryRecyclerView(binding)

        binding.addCategoryFab.setOnClickListener {
            val toEditCategory =
                CategoryListFragmentDirections.actionToEditCategoryFragment(Category().asParcel())
            findNavController().navigate(toEditCategory)
        }
    }

    private fun setupCategoryRecyclerView(binding: CategoryListFragmentBinding) {
        binding.categoryList.apply {
            val categoryAdapter = CategoryAdapter(
                onWriteNfcClicked = ::showWriteNfcActivityAsDialog,
                onEditClicked = ::showEditCategoryDialog,
                onDeleteClicked = ::showDeleteConfirmationDialog
            )

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
        val inflater = requireActivity().layoutInflater
        val inputBinding = CategoryEditNameDialogBinding.inflate(inflater)

        AlertDialog.Builder(requireContext())
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
                    if (input.isBlank()) return@let
                    viewModel.saveCategory(category.withName(input))
                }
            }
            .setView(inputBinding.root)
            .show()
    }

    private fun showDeleteConfirmationDialog(category: Category) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_delete_title, category.name))
            .setMessage(getString(R.string.dialog_delete_message, category.name))
            .setNegativeButton(R.string.dialog_delete_button_cancel, null)
            .setPositiveButton(R.string.dialog_delete_button_delete) { _, _ ->
                viewModel.onDeleteMenuItemClicked(category)
            }
            .create()
            .show()
    }

}