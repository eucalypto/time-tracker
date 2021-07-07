package net.eucalypto.timetracker.categories.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.databinding.CategoryListItemBinding
import net.eucalypto.timetracker.domain.model.Category

class CategoryAdapter(
    private val onWriteNfcClicked: (Category) -> Unit,
    private val onEditClicked: (Category) -> Unit,
    private val onDeleteClicked: (Category) -> Unit
) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem, onWriteNfcClicked, onEditClicked, onDeleteClicked)
    }
}


class CategoryViewHolder
private constructor(private val binding: CategoryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var category: Category
    private val context = binding.root.context

    fun bind(
        category: Category,
        onWriteNfcClicked: (Category) -> Unit,
        onEditClicked: (Category) -> Unit,
        onDeleteClicked: (Category) -> Unit
    ) {
        this.category = category
        binding.category = category
        binding.contextMenuButton.setOnClickListener {
            showContextMenu(
                it,
                onEditClicked,
                onWriteNfcClicked,
                onDeleteClicked
            )
        }
        binding.root.setOnLongClickListener {
            showContextMenu(
                binding.contextMenuButton,
                onEditClicked,
                onWriteNfcClicked,
                onDeleteClicked
            )
            true
        }
    }

    private fun showContextMenu(
        it: View,
        onEditClicked: (Category) -> Unit,
        onWriteNfcClicked: (Category) -> Unit,
        onDeleteClicked: (Category) -> Unit
    ) {
        val menu = PopupMenu(context, it)
        menu.inflate(R.menu.category_list_item_menu)
        menu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_edit -> {
                    onEditClicked(category)
                }
                R.id.menu_item_delete -> {
                    onDeleteClicked(category)
                }
                R.id.menu_item_write_nfc -> {
                    onWriteNfcClicked(category)
                }
            }
            true
        }
        menu.show()
    }

    companion object {
        fun from(parent: ViewGroup): CategoryViewHolder {
            val binding =
                CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CategoryViewHolder(binding)
        }
    }
}

class CategoryDiffCallback : ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}


@BindingAdapter("categoryList")
fun bindCategoryList(recycler: RecyclerView, list: List<Category>?) {
    (recycler.adapter as CategoryAdapter).submitList(list)
}