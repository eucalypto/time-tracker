package net.eucalypto.timetracker.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.eucalypto.timetracker.database.WorkStatusDatabase
import net.eucalypto.timetracker.databinding.AddEventFragmentBinding

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
        val binding = AddEventFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DataBindingUtil.getBinding<AddEventFragmentBinding>(view)!!

        binding.button2.setOnClickListener {
            val action = AddEventFragmentDirections.actionToWriteNfcActivity()
            findNavController().navigate(action)
        }

        viewModel.onIntentReceived(requireActivity().intent)
    }
}