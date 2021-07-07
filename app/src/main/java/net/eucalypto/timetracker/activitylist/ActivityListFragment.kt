package net.eucalypto.timetracker.activitylist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import net.eucalypto.timetracker.R
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

        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activity_list_actionbar_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }

}