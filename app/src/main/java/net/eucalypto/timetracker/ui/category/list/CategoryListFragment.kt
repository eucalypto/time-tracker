package net.eucalypto.timetracker.ui.category.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.data.getRepository
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

    private fun setupRecyclerView(categoryList: RecyclerView) {
        val categoryAdapter = CategoryAdapter(viewModel)
        categoryList.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupAddCategoryFab(binding: CategoryListFragmentBinding) {
        binding.addCategoryFab.setOnClickListener {
            val toCreateCategory =
                CategoryListFragmentDirections
                    .actionToCreateCategoryFragment(Category().asParcel())
            findNavController().navigate(toCreateCategory)
        }
    }
}


