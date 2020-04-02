package com.grsu.guideapp.ui.holders.`object`

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.base.BaseViewHolder
import com.grsu.guideapp.data.local.database.vo.ObjectItemVO
import com.squareup.picasso.Picasso

class ObjectHolder(
    itemView: View,
    listener: ((View, Int) -> Unit)
) : BaseViewHolder<ObjectItemVO>(itemView, listener) {

    private val image = itemView.findViewById(R.id.item_type_image) as? ImageView
    private val name = itemView.findViewById(R.id.item_type_name) as? TextView

    override fun bind(item: ObjectItemVO) {
        name?.text = item.name
        val creator = Picasso.get().load(item.image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
        if (image != null) creator.into(image)
    }
}