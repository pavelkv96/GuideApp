package com.grsu.guideapp.ui.holders.routes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.base.BaseViewHolder
import com.grsu.guideapp.data.local.database.vo.RouteItemVO
import com.grsu.guideapp.utils.extensions.toDistance
import com.grsu.guideapp.utils.extensions.toDuration

class RoutesViewHolder(
    itemView: View,
    listener: (View, Int) -> Unit
) : BaseViewHolder<RouteItemVO>(itemView, listener) {

    private val title = itemView.findViewById(R.id.tv_item_route_name) as? TextView
    private val distance = itemView.findViewById(R.id.tv_item_route_distance) as? TextView
    private val duration = itemView.findViewById(R.id.tv_item_route_duration) as? TextView
    private val image = itemView.findViewById(R.id.iv_item_route_image) as? ImageView

    override fun bind(item: RouteItemVO) = item.let {
        title?.text = it.name
        distance?.text = it.distance.toDistance()
        duration?.text = it.duration.toDuration()

//        val file = File(Settings.CONTENT, it.image.toMD5())
//        val creator = Picasso.get().load(file)
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .error(R.drawable.ic_launcher_foreground)
//        if (image != null){
//            creator.into(image)
//        }
    }
}