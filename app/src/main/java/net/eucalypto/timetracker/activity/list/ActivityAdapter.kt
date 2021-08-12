package net.eucalypto.timetracker.activity.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.databinding.ActivityListItemBinding
import net.eucalypto.timetracker.domain.model.Activity

class ActivityAdapter(
    private val sharedViewModel: ActivityListViewModel
) :
    ListAdapter<Activity, ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.from(parent, sharedViewModel)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

}

class ActivityDiffCallback : DiffUtil.ItemCallback<Activity>() {

    override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
        return oldItem == newItem
    }

}

class ActivityViewHolder(
    private val binding: ActivityListItemBinding,
    private val sharedViewModel: ActivityListViewModel
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var activity: Activity

    fun bindTo(item: Activity) {
        activity = item
        binding.activity = item
        binding.activityContextButton.setOnClickListener {
            showContextMenu(it)
        }
        binding.root.setOnLongClickListener {
            showContextMenu(binding.activityContextButton)
            true
        }
    }

    private fun showContextMenu(view: View) {

        PopupMenu(view.context, view).apply {
            inflate(R.menu.activity_list_item_popup_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_edit_start_time -> {
                        sharedViewModel.setChosenActivityForStartTime(activity)
                        showTimePickerDialog()
                        true
                    }
                    R.id.menu_item_edit_end_time -> {
                        sharedViewModel.setChosenActivityForEndTime(activity)
                        showTimePickerDialog()
                        true
                    }
                    R.id.menu_item_delete -> {
                        sharedViewModel.chosenActivity = activity
                        showDeleteActivityDialog()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            show()
        }
    }

    private fun showDeleteActivityDialog() {
        val toDeleteActivityDialog =
            ActivityListFragmentDirections.actionActivityListFragmentToDeleteActivityConfirmationDialogFragment()
        binding.root.findNavController().navigate(toDeleteActivityDialog)
    }

    private fun showTimePickerDialog() {
        val toTimePickerDialog =
            ActivityListFragmentDirections
                .actionActivityListFragmentToTimePickerDialogFragment()
        binding.root.findNavController().navigate(toTimePickerDialog)
    }

    companion object {
        fun from(viewGroup: ViewGroup, sharedViewModel: ActivityListViewModel): ActivityViewHolder {
            val binding = ActivityListItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return ActivityViewHolder(binding, sharedViewModel)
        }
    }
}
