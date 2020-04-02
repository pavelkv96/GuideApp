package com.grsu.guideapp.ui.holders.favorite

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.base.BaseViewHolder

class FavoriteHolder(
    itemView: View,
    listener: ((View, Int) -> Unit)
) : BaseViewHolder<String>(itemView, listener) {

    private var image = itemView.findViewById(R.id.aciv_item_favorite_image) as? AppCompatImageView
    private var name = itemView.findViewById(R.id.mtv_item_favorite_name) as? MaterialTextView


    override fun bind(item: String) {
        name?.text = item
    }
}
