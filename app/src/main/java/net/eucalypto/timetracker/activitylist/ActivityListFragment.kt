package net.eucalypto.timetracker.activitylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding

class ActivityListFragment : Fragment() {

    private val viewModel: ActivityListViewModel by viewModels() {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ActivityListFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ActivityListFragmentBinding.bind(view)

        setupRecyclerView(binding)
    }

    private fun setupRecyclerView(binding: ActivityListFragmentBinding) {
        binding.activityList.adapter = ActivityAdapter()
        binding.activityList.layoutManager = LinearLayoutManager(context)

        viewModel.activityList.observe(viewLifecycleOwner) {
            (binding.activityList.adapter as ActivityAdapter).submitList(it)
        }
    }

}