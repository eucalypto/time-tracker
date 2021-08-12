package net.eucalypto.timetracker.ui.category.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.R
import net.eucalypto.timetracker.databinding.CategoryListItemBinding
import net.eucalypto.timetracker.domain.model.Category
import net.eucalypto.timetracker.domain.model.util.asParcel

class CategoryAdapter(
    private val sharedViewModel: CategoryListViewModel
) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent, sharedViewModel)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem)
    }
}


class CategoryViewHolder
private constructor(
    private val binding: CategoryListItemBinding,
    private val sharedViewModel: CategoryListViewModel
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var category: Category

    fun bind(category: Category) {
        this.category = category
        binding.category = category
        binding.contextMenuButton.setOnClickListener {
            showContextMenu(it)
        }
        binding.root.setOnLongClickListener {
            showContextMenu(binding.contextMenuButton)
            true
        }
    }

    private fun showContextMenu(view: View) {
        val menu = PopupMenu(view.context, view)
        menu.inflate(R.menu.category_list_item_popup_menu)
        menu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_edit -> {
                    sharedViewModel.categoryToUpdate = category
                    val toEditCategory =
                        CategoryListFragmentDirections.actionToEditCategoryDialogFragment()
                    binding.root.findNavController().navigate(toEditCategory)
                }
                R.id.menu_item_delete -> {
                    sharedViewModel.categoryToDelete = category
                    val toDeleteConfirmationDialog =
                        CategoryListFragmentDirections.actionToDeleteCategoryConfirmationDialogFragment()
                    binding.root.findNavController().navigate(toDeleteConfirmationDialog)
                }
                R.id.menu_item_write_nfc -> {
                    val toWriteNfc =
                        CategoryListFragmentDirections.actionToWriteNfcActivity(category.asParcel())
                    binding.root.findNavController().navigate(toWriteNfc)
                }
            }
            true
        }
        menu.show()
    }

    companion object {
        fun from(parent: ViewGroup, viewModel: CategoryListViewModel): CategoryViewHolder {
            val binding =
                CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CategoryViewHolder(binding, viewModel)
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