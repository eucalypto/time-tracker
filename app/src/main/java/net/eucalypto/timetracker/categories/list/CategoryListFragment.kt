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
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.data.database.getDatabase
import net.eucalypto.timetracker.databinding.CategoryListFragmentBinding
import net.eucalypto.timetracker.domain.model.Category
import net.eucalypto.timetracker.domain.model.util.asParcel

class CategoryListFragment : Fragment() {

    private val viewModel: CategoryListViewModel by viewModels {
        val database = getDatabase(requireContext().applicationContext)
        CategoryListViewModel.Factory(Repository(database))
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
                onWriteNfcButtonClicked = { category ->
                    val toWriteNfc =
                        CategoryListFragmentDirections.actionToWriteNfcActivity(category.asParcel())
                    findNavController().navigate(toWriteNfc)
                },
                onEditButtonClicked = { existingCategory ->
                    val toEditCategory =
                        CategoryListFragmentDirections.actionToEditCategoryFragment(
                            existingCategory.asParcel()
                        )
                    findNavController().navigate(toEditCategory)
                },
                onDeleteButtonClicked = {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.dialog_delete_title, it.name))
                        .setMessage(getString(R.string.dialog_delete_message, it.name))
                        .setNegativeButton(R.string.dialog_delete_button_cancel, null)
                        .setPositiveButton(R.string.dialog_delete_button_delete) { _, _ ->
                            viewModel.onDeleteMenuItemClicked(it)
                        }
                        .create()
                        .show()
                }
            )

            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}