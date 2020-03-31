package com.grsu.guideapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grsu.guideapp.R
import com.grsu.guideapp.holders.favorite.FavoriteHolder

class FavoriteAdapter(
    private val listener: ((View, Int) -> Unit)
) : ListAdapter<String, FavoriteHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteHolder(view, listener)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    public override fun getItem(position: Int): String? = currentList.getOrNull(position)
}

private class FavoriteDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}