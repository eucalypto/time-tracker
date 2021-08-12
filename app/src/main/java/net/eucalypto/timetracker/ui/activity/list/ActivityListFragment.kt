package net.eucalypto.timetracker.ui.activity.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.data.getRepository
import net.eucalypto.timetracker.databinding.ActivityListFragmentBinding

class ActivityListFragment : Fragment() {

    private val viewModel: ActivityListViewModel by activityViewModels {
        ActivityListViewModelFactory(getRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActivityListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val binding = DataBindingUtil.getBinding<ActivityListFragmentBinding>(view)!!

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView(binding.activityList)

        viewModel.editTimeError.observe(viewLifecycleOwner) { editTimeError ->
            showErrorSnackbar(editTimeError)
        }
    }

    private fun setupRecyclerView(activityList: RecyclerView) {
        activityList.adapter = ActivityAdapter(viewModel)
        activityList.layoutManager = LinearLayoutManager(context)
    }

    private fun showErrorSnackbar(editTimeError: EditTimeError?) {
        showSnackbarWithText(
            when (editTimeError) {
                EditTimeError.FUTURE_TIME -> {
                    R.string.activity_edit_dialog_start_or_end_time_in_the_future
                }
                EditTimeError.START_AFTER_END -> {
                    R.string.activity_edit_dialog_end_time_before_start_time
                }
                else -> {
                    return
                }
            }
        )
        viewModel.resetEditTimeError()
    }

    private fun showSnackbarWithText(resId: Int) {
        Snackbar
            .make(
                requireView(),
                resId,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(R.string.ok_button) {}
            .show()
    }
}