package com.grsu.guideapp.ui.holders.favorite

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.vo.FavoriteVO
import com.grsu.guideapp.utils.base.BaseViewHolder
import com.squareup.picasso.Picasso

class FavoriteHolder(
    itemView: View,
    listener: ((View, Int) -> Unit)
) : BaseViewHolder<FavoriteVO>(itemView, listener) {

    private var image = itemView.findViewById(R.id.aciv_item_favorite_image) as? AppCompatImageView
    private var name = itemView.findViewById(R.id.mtv_item_favorite_name) as? MaterialTextView


    override fun bind(item: FavoriteVO) {
        itemView.tag = item.id
        name?.text = item.name
        image?.also {
            Picasso.get().load(item.photo)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(it)
        }
    }
}
