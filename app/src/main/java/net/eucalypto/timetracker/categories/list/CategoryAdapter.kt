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
    private val onWriteNfcButtonClicked: (Category) -> Unit,
    private val onEditButtonClicked: (Category) -> Unit,
    private val onDeleteButtonClicked: (Category) -> Unit
) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem, onWriteNfcButtonClicked, onEditButtonClicked, onDeleteButtonClicked)
    }
}


class CategoryViewHolder
private constructor(private val binding: CategoryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var category: Category
    private val context = binding.root.context

    fun bind(
        category: Category,
        onWriteNfcButtonClicked: (Category) -> Unit,
        onEditButtonClicked: (Category) -> Unit,
        onDeleteButtonClicked: (Category) -> Unit
    ) {
        this.category = category
        binding.categoryName.text = category.name
        binding.contextMenuButton.setOnClickListener {
            showContextMenu(
                it,
                onEditButtonClicked,
                onWriteNfcButtonClicked,
                onDeleteButtonClicked
            )
        }
        binding.root.setOnLongClickListener {
            showContextMenu(
                binding.contextMenuButton,
                onEditButtonClicked,
                onWriteNfcButtonClicked,
                onDeleteButtonClicked
            )
            true
        }
    }

    private fun showContextMenu(
        it: View,
        onEditButtonClicked: (Category) -> Unit,
        onWriteNfcButtonClicked: (Category) -> Unit,
        onDeleteButtonClicked: (Category) -> Unit
    ) {
        val menu = PopupMenu(context, it)
        menu.inflate(R.menu.category_list_item_menu)
        menu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_edit -> {
                    onEditButtonClicked(category)
                }
                R.id.menu_item_delete -> {
                    onDeleteButtonClicked(category)
                }
                R.id.menu_item_write_nfc -> {
                    onWriteNfcButtonClicked(category)
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