package net.eucalypto.timetracker.categories.editcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.data.database.getDatabase
import net.eucalypto.timetracker.databinding.EditCategoryFragmentBinding

class EditCategoryFragment : Fragment() {

    private val viewModel: EditCategoryViewModel by viewModels {
        val dao = getDatabase(requireContext().applicationContext).categoryDao
        val repository = Repository(dao)
        EditCategoryViewModel.Factory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EditCategoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.getBinding<EditCategoryFragmentBinding>(view)!!

        binding.viewModel = viewModel
    }

    override fun onStop() {
        super.onStop()

        viewModel.saveCategory()
    }
}