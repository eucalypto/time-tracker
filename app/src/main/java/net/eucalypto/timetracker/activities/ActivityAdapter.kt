package net.eucalypto.timetracker.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.databinding.ActivityListItemBinding
import net.eucalypto.timetracker.domain.model.Activity

class ActivityAdapter(private val onDeleteClicked: (Activity) -> Unit) :
    ListAdapter<Activity, ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bindTo(getItem(position), onDeleteClicked)
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

class ActivityViewHolder(val binding: ActivityListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var activity: Activity

    fun bindTo(item: Activity, onDeleteClicked: (Activity) -> Unit) {
        activity = item
        binding.activity = item
        binding.activityContextButton.setOnClickListener {
            showContextMenu(it, onDeleteClicked)
        }
    }

    private fun showContextMenu(view: View, onDeleteClicked: (Activity) -> Unit) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.activity_list_item_popup_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_delete -> {
                        onDeleteClicked(activity)
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

    companion object {
        fun from(viewGroup: ViewGroup): ActivityViewHolder {
            val binding = ActivityListItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
            return ActivityViewHolder(binding)
        }
    }

}


