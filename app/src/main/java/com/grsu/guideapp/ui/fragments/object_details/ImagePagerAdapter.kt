package com.grsu.guideapp.ui.fragments.object_details

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.vo.ImagesVO
import com.grsu.guideapp.project_settings.Settings
import com.squareup.picasso.Picasso

internal class ImagePagerAdapter(
    var images: List<ImagesVO>,
    val listener: ((View, Int) -> Unit),
    private val picasso: Picasso
) : PagerAdapter(), View.OnClickListener {

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount() = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = ImageView(container.context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener(this@ImagePagerAdapter)
            tag = position
        }
        container.addView(v, 0)

        Settings.CACHE_FOLDER
        picasso.load(images[position].reference)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) = container.removeView(view as View)

    override fun onClick(view: View?) {
        view?.let { listener.invoke(it, it.tag as Int) }
    }

    fun submitList(entities: List<ImagesVO>) {
        images = entities
        notifyDataSetChanged()
    }
}