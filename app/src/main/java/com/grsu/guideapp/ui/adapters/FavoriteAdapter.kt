package com.grsu.guideapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.vo.FavoriteVO
import com.grsu.guideapp.ui.holders.favorite.FavoriteHolder

class FavoriteAdapter(
    private val listener: ((View, Int) -> Unit)
) : ListAdapter<FavoriteVO, FavoriteHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteHolder(view, listener)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    public override fun getItem(position: Int): FavoriteVO? = currentList.getOrNull(position)
}

private class FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteVO>() {

    override fun areItemsTheSame(oldItem: FavoriteVO, newItem: FavoriteVO) = oldItem == newItem

    override fun areContentsTheSame(oldItem: FavoriteVO, newItem: FavoriteVO) = oldItem.id == newItem.id
}