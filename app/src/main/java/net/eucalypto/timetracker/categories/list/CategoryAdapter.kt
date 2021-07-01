package net.eucalypto.timetracker.categories.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.eucalypto.timetracker.databinding.CategoryListItemBinding
import net.eucalypto.timetracker.domain.model.Category

class CategoryAdapter(
    private val onWriteNfcButtonClicked: (Category) -> Unit,
    private val onEditButtonClicked: (Category) -> Unit
) :
    ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem, onWriteNfcButtonClicked, onEditButtonClicked)
    }
}


class CategoryViewHolder
private constructor(private val binding: CategoryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var category: Category

    fun bind(
        category: Category,
        onWriteNfcButtonClicked: (Category) -> Unit,
        onEditButtonClicked: (Category) -> Unit
    ) {
        this.category = category
        binding.categoryName.text = category.name
        binding.writeNfcButton.setOnClickListener { onWriteNfcButtonClicked(category) }
        binding.editButton.setOnClickListener { onEditButtonClicked(category) }
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