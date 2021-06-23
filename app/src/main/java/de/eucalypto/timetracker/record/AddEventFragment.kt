package de.eucalypto.timetracker.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import de.eucalypto.timetracker.database.WorkStatusDatabase
import de.eucalypto.timetracker.databinding.FragmentAddEventBinding

class AddEventFragment : Fragment() {

    private val viewModel: AddEventViewModel
            by viewModels {
                val workDao =
                    WorkStatusDatabase.getInstance(requireContext().applicationContext).workStatusDatabaseDao
                AddEventViewModelFactory(workDao)
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddEventBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.getBinding<FragmentAddEventBinding>(view)!!

        viewModel.onIntentReceived(requireActivity().intent)
    }
}