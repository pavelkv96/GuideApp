package com.grsu.guideapp.fragments.object_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.R

class DetailsPagerAdapter(
    private var contentList: List<ContentItem> = listOf(),
    private val listener: ((View, Int) -> Unit)
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = contentList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val data = contentList[position]
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_object_other, container, false)
        val content = view.findViewById<MaterialTextView>(R.id.mtv_item_object_other_content)
        val action = view.findViewById<MaterialTextView>(R.id.mtv_item_object_other_action)
        with(content) { text = data.content }
        with(action) {
            tag = position
            text = data.action
            setOnClickListener { view -> view?.let { listener.invoke(view, view.tag as Int) } }
        }
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) = container.removeView(view as View)

    fun submitList(entities: List<ContentItem>) {
        contentList = entities
        notifyDataSetChanged()
    }
}