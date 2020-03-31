package com.grsu.guideapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grsu.guideapp.R
import com.grsu.guideapp.fragments.order.CheckedItem
import com.grsu.guideapp.holders.order.OrderViewHolder

class OrderAdapter(
    private val listener: ((View, Int) -> Unit),
    private val listenerChoice: (Boolean, Int) -> Unit
) : ListAdapter<CheckedItem, OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view, listener, listenerChoice)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    public override fun getItem(position: Int) = currentList.getOrNull(position)

    fun getAllChecked(): List<CheckedItem> {
        return if (currentList.isNotEmpty()) currentList.filter { !it.isCheck } else listOf()
    }

    fun update(position: Int): Unit = run {
        currentList.getOrNull(position)?.also {
            it.isCheck = it.isCheck.not()
            notifyItemChanged(position)
        }
    }
}

private class OrderDiffCallback : DiffUtil.ItemCallback<CheckedItem>() {

    override fun areItemsTheSame(oldItem: CheckedItem, newItem: CheckedItem) = oldItem.item.id == newItem.item.id

    override fun areContentsTheSame(oldItem: CheckedItem, newItem: CheckedItem) = oldItem.item.id == newItem.item.id
}