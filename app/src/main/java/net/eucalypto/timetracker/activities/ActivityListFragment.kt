package net.eucalypto.timetracker.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding
import net.eucalypto.timetracker.domain.model.Activity

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

        val binding = DataBindingUtil.getBinding<ActivityListFragmentBinding>(view)!!
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView(binding)
    }

    private fun setupRecyclerView(binding: ActivityListFragmentBinding) {
        binding.activityList.adapter = ActivityAdapter(
            onDeleteClicked = ::showDeleteConfirmationDialog
        )
        binding.activityList.layoutManager = LinearLayoutManager(context)
    }

    private fun showDeleteConfirmationDialog(it: Activity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.activity_list_dialog_delete_title)
            .setMessage(R.string.activity_list_dialog_delete_message)
            .setPositiveButton(R.string.dialog_delete_button_delete) { _, _ ->
                viewModel.delete(it)
            }
            .setNegativeButton(R.string.dialog_delete_button_cancel) { _, _ -> }
            .show()
    }

}