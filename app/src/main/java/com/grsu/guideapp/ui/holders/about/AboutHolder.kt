package com.grsu.guideapp.ui.holders.about

import android.view.View
import android.widget.TextView
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.base.BaseViewHolder
import com.grsu.guideapp.ui.fragments.about.AboutItem

class AboutHolder(
    itemView: View,
    listener: ((View, Int) -> Unit)
) : BaseViewHolder<AboutItem>(itemView, listener) {

    private var title = itemView.findViewById<TextView>(R.id.title)
    private var action = itemView.findViewById<TextView>(R.id.description)


    override fun bind(item: AboutItem) {
        itemView.context?.apply {
            title.text = getString(item.title)
            action.text = getString(item.action)
        }
    }
}