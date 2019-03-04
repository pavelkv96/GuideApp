package com.grsu.guideapp.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.StorageUtils;

public class RouteViewHolder extends ViewHolder {

    private ImageView iv_item_preview_photo_route;
    private TextView tv_item_routes_duration;
    private TextView tv_item_routes_distance;
    private TextView tv_item_routes_name_route;

    public RouteViewHolder(@NonNull View pView) {
        super(pView);

        iv_item_preview_photo_route = pView.findViewById(R.id.iv_item_preview_photo_route);
        tv_item_routes_duration = pView.findViewById(R.id.tv_item_routes_duration);
        tv_item_routes_distance = pView.findViewById(R.id.tv_item_routes_distance);
        tv_item_routes_name_route = pView.findViewById(R.id.tv_item_routes_name_route);
    }

    public void bind(final Route route) {
        tv_item_routes_duration.setText(toDuration(route.getDuration()));
        tv_item_routes_distance.setText(toDistance(route.getDistance()));
        tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute()));

        String photo = route.getReferencePhotoRoute();

        try {
            iv_item_preview_photo_route.setImageBitmap(StorageUtils.getImageFromFile(photo));
        } catch (NullPointerException e) {
            iv_item_preview_photo_route.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private String toDistance(Integer distance) {
        if (distance > 900) {
            return String.format("%s km", distance / 1000);
        }
        return String.format("%s m", distance);
    }

    private String toDuration(Integer distance) {
        if (distance > 60) {
            return String.format("%s h %s min", distance / 60, distance % 60);
        }
        return String.format("%s min", distance);
    }
}
