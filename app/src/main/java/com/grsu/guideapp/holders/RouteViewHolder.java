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
    private TextView tv_item_routes_id_author;
    private TextView tv_item_routes_distance;
    private TextView tv_item_routes_name_route;

    public RouteViewHolder(@NonNull View pView) {
        super(pView);

        iv_item_preview_photo_route = pView.findViewById(R.id.iv_item_preview_photo_route);
        tv_item_routes_id_author = pView.findViewById(R.id.tv_item_routes_id_author);
        tv_item_routes_distance = pView.findViewById(R.id.tv_item_routes_distance);
        tv_item_routes_name_route = pView.findViewById(R.id.tv_item_routes_name_route);
    }

    public void bind(final Route route) {
        tv_item_routes_id_author.setText(String.valueOf(route.getIdAuthor()));
        tv_item_routes_distance.setText(String.valueOf(route.getDistance()));
        tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute()));

        String photo = route.getReferencePhotoRoute();

        try {
            iv_item_preview_photo_route.setImageBitmap(StorageUtils.getImageFromFile(photo));
        } catch (NullPointerException e) {
            iv_item_preview_photo_route.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
