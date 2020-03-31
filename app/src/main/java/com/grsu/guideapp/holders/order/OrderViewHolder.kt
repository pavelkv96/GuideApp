package com.grsu.guideapp.holders.order

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.R
import com.grsu.guideapp.base.BaseViewHolder
import com.grsu.guideapp.fragments.order.CheckedItem
import com.grsu.guideapp.utils.extensions.toBitmap

class OrderViewHolder(
    itemView: View,
    listener: ((View, Int) -> Unit),
    private val listenerChoice: ((Boolean, Int) -> Unit)
) : BaseViewHolder<CheckedItem>(itemView, listener) {

    private val image = itemView.findViewById(R.id.aciv_item_order_image) as? AppCompatImageView
    private val name = itemView.findViewById(R.id.mtv_item_order_name) as? MaterialTextView
    private val choice = itemView.findViewById(R.id.mcb_item_order_choice) as? MaterialCheckBox

    override fun bind(item: CheckedItem): Unit = item.let {
        name?.text = it.item.name
        val bytes = it.item.image
        if (bytes.isEmpty()) image?.setImageResource(R.drawable.ic_launcher_foreground)
        else image?.setImageBitmap(it.item.image.toBitmap())
        choice?.isChecked = it.isCheck
        choice?.setOnCheckedChangeListener { _, isChecked -> listenerChoice.invoke(isChecked, adapterPosition) }
    }
}