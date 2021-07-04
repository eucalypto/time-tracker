package net.eucalypto.timetracker.activitylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.databinding.ActivityListItemBinding
import net.eucalypto.timetracker.domain.model.Activity

class ActivityAdapter : ListAdapter<Activity, ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.from(parent)
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

class ActivityViewHolder(val binding: ActivityListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(item: Activity) {
        binding.activity = item
        binding.executePendingBindings()
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
