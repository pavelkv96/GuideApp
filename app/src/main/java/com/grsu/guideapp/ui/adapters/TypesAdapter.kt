package com.grsu.guideapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.vo.TypeItemVO
import com.grsu.guideapp.ui.holders.types.TypeHolder

class TypesAdapter(
    private var typesItems: List<TypeItemVO> = mutableListOf(),
    private val listener: ((View, Int) -> Unit)
) : Adapter<TypeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        return TypeHolder(view, listener)
    }

    override fun onBindViewHolder(holder: TypeHolder, position: Int) {
        holder.bind(typesItems[position])
    }

    override fun getItemCount(): Int = typesItems.size

    fun submitList(entities: List<TypeItemVO>) {
        val diffCallback = DiffCallback(typesItems, entities)
        val diffUtilsResult = DiffUtil.calculateDiff(diffCallback)
        typesItems = entities
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int) = if (position <= typesItems.size) typesItems[position] else null

    internal inner class DiffCallback(
        private val oldList: List<TypeItemVO>,
        private val newList: List<TypeItemVO>
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