package net.eucalypto.timetracker.categories.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.eucalypto.timetracker.databinding.CategoryListFragmentBinding

class CategoryListFragment : Fragment() {

    private val viewModel: CategoryListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CategoryListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.getBinding<CategoryListFragmentBinding>(view)!!

        setupCategoryRecyclerView(binding)

        binding.addCategoryFab.setOnClickListener {
            val action = CategoryListFragmentDirections.actionToEditCategoryFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupCategoryRecyclerView(binding: CategoryListFragmentBinding) {
        binding.categoryList.apply {
            adapter = CategoryAdapter(
                onWriteNfcButtonClicked = {
                    val action =
                        CategoryListFragmentDirections.actionToWriteNfcActivity(it.toString())
                    findNavController().navigate(action)
                }
            ).apply {
                submitList(viewModel.dummyList)
            }

            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}