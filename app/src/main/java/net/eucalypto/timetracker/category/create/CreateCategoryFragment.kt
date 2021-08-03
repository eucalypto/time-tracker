package net.eucalypto.timetracker.category.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import net.eucalypto.timetracker.data.Repository
import net.eucalypto.timetracker.data.database.getDatabase
import net.eucalypto.timetracker.databinding.CreateCategoryFragmentBinding
import net.eucalypto.timetracker.domain.model.util.asCategory

class CreateCategoryFragment : Fragment() {

    private val args: CreateCategoryFragmentArgs by navArgs()

    private val viewModel: CreateCategoryViewModel by viewModels {
        val database = getDatabase(requireContext().applicationContext)
        val repository = Repository(database)
        CreateCategoryViewModel.Factory(repository, args.categoryParcel.asCategory())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CreateCategoryFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                requireActivity().onBackPressed()
            }
        }
    }
}