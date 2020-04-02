package com.grsu.guideapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.vo.RouteItemVO
import com.grsu.guideapp.ui.holders.routes.RoutesViewHolder

class AllRoutesListAdapter(
    private var routesItems: List<RouteItemVO> = listOf(),
    private val listener: (View, Int) -> Unit
) : Adapter<RoutesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RoutesViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: RoutesViewHolder, position: Int) = holder.bind(routesItems[position])

    override fun getItemCount(): Int = routesItems.size

    fun submitList(entities: List<RouteItemVO>) {
        val diffCallback = DiffCallback(routesItems, entities)
        val diffUtilsResult = DiffUtil.calculateDiff(diffCallback)
        routesItems = entities
        diffUtilsResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int) = if (position <= routesItems.size) routesItems[position] else null

    internal inner class DiffCallback(
        private val oldList: List<RouteItemVO>,
        private val newList: List<RouteItemVO>
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