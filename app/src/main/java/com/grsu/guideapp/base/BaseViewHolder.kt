package com.grsu.guideapp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder<T>(
    itemView: View,
    protected val listener: ((View, Int) -> Unit)?
) : ViewHolder(itemView) {

    init {
        if (listener != null) {
            itemView.setOnClickListener { listener.invoke(it, adapterPosition) }
        }
    }

    abstract fun bind(item: T)
}