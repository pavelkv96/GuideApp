package com.grsu.guideapp.holders.types

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.grsu.guideapp.R
import com.grsu.guideapp.base.BaseViewHolder
import com.grsu.guideapp.database.vo.TypeItemVO
import com.grsu.guideapp.utils.extensions.toBitmap

class TypeHolder(
    itemView: View,
    listener: ((View, Int) -> Unit)
) : BaseViewHolder<TypeItemVO>(itemView, listener) {

    private val name = itemView.findViewById(R.id.item_type_name) as? TextView
    private val image = itemView.findViewById(R.id.item_type_image) as? ImageView

    override fun bind(item: TypeItemVO): Unit = item.let {
        name?.text = it.name
        val bytes = it.image
        if (bytes.isEmpty()) image?.setImageResource(R.drawable.ic_launcher_foreground)
        else image?.setImageBitmap(it.image.toBitmap())
    }
}