package com.grsu.guideapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.grsu.guideapp.R
import com.grsu.guideapp.database.vo.ObjectItemVO
import com.grsu.guideapp.holders.`object`.ObjectHolder


class ObjectAdapter(
    private var objectItems: List<ObjectItemVO> = mutableListOf(),
    private val listener: ((View, Int) -> Unit)
) : Adapter<ObjectHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object, parent, false)
        return ObjectHolder(view, listener)
    }

    override fun getItemCount() = objectItems.size

    override fun onBindViewHolder(holder: ObjectHolder, position: Int) {
        holder.bind(objectItems[position])
    }

    fun getItem(position: Int) = objectItems.getOrNull(position)

    fun submitList(entities: List<ObjectItemVO>) {
        val diffCallback = DiffCallback(objectItems, entities)
        val diffUtilsResult = DiffUtil.calculateDiff(diffCallback)
        objectItems = entities
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    inner class DiffCallback(
        private val oldList: List<ObjectItemVO>,
        private val newList: List<ObjectItemVO>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}