package de.eucalypto.timetracker.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.eucalypto.timetracker.database.WorkStatusDatabase
import de.eucalypto.timetracker.databinding.FragmentAddEventBinding

class AddEventFragment : Fragment() {

    lateinit var viewModel: AddEventViewModel
    lateinit var binding: FragmentAddEventBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpViewModel()

        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }

    private fun setUpViewModel() {
        val application = requireActivity().application
        val dataSource = WorkStatusDatabase.getInstance(application).workStatusDatabaseDao
        val viewModelFactory = AddEventViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AddEventViewModel::class.java)
        viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onIntentReceived(requireActivity().intent)
    }
}