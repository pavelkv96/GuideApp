package com.grsu.guideapp.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.models.Route;
import com.squareup.picasso.Picasso;

public class RouteViewHolder extends ViewHolder {

    private ImageView iv_item_preview_photo_route;
    private TextView tv_item_routes_id_author;
    private TextView tv_item_routes_distance;
    private TextView tv_item_routes_name_route;
    private View mView;

    public RouteViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        iv_item_preview_photo_route = mView.findViewById(R.id.iv_item_preview_photo_route);
        tv_item_routes_id_author = mView.findViewById(R.id.tv_item_routes_id_author);
        tv_item_routes_distance = mView.findViewById(R.id.tv_item_routes_distance);
        tv_item_routes_name_route = mView.findViewById(R.id.tv_item_routes_name_route);
    }

    public void bind(final Route route) {
        tv_item_routes_id_author.setText(String.valueOf(route.getIdAuthor()));
        tv_item_routes_distance.setText(String.valueOf(route.getDistance()));
        tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute()));

        Picasso
                .get()
                .load(route.getReferencePhotoRoute())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(iv_item_preview_photo_route);
    }
}
