package com.grsu.guideapp.holders.about

import android.view.View
import android.widget.TextView
import com.grsu.guideapp.R
import com.grsu.guideapp.base.NewBaseViewHolder
import com.grsu.guideapp.fragments.about.AboutItem

class AboutHolder(
    itemView: View,
    listener: ((View, Int) -> Unit)
) : NewBaseViewHolder<AboutItem>(itemView, listener) {

    private var title = itemView.findViewById<TextView>(R.id.title)
    private var action = itemView.findViewById<TextView>(R.id.description)


    override fun bind(item: AboutItem) {
        itemView.context?.apply {
            title.text = getString(item.title)
            action.text = getString(item.action)
        }
    }
}