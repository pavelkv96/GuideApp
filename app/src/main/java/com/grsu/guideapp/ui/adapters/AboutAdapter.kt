package com.grsu.guideapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.fragments.about.AboutItem
import com.grsu.guideapp.ui.holders.about.AboutHolder

class AboutAdapter(
    private var aboutItems: List<AboutItem>,
    private val listener: (View, Int) -> Unit
) : RecyclerView.Adapter<AboutHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_about, parent, false)
        return AboutHolder(view, listener)
    }

    override fun onBindViewHolder(holder: AboutHolder, position: Int) = holder.bind(aboutItems[position])

    override fun getItemCount(): Int = aboutItems.size
}