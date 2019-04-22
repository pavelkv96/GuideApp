package com.grsu.guideapp.holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import com.squareup.picasso.Picasso;
import java.io.File;

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

    public void bind(final Route route, final Context context) {
        tv_item_routes_duration.setText(toDuration(route.getDuration(), context));
        tv_item_routes_distance.setText(toDistance(route.getDistance(), context));
        tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute()));

        String photo = CryptoUtils.hash(route.getReferencePhotoRoute());
        File file = new File(Settings.CONTENT, photo);

        Picasso.get().load(file)
                .placeholder(R.drawable.my_location)
                .error(R.drawable.ic_launcher_background)
                .into(iv_item_preview_photo_route);
    }

    private String toDistance(Integer distance, Context context) {
        String pattern = "%s %s";
        if (distance > 900) {
            return String.format(pattern, distance / 1000, context.getString(R.string.short_kilometers));
        }
        return String.format(pattern, distance, context.getString(R.string.short_meters));
    }

    private String toDuration(Integer distance, Context context) {
        String pattern = "%s %s";
        if (distance > 60) {
            return String.format("%s %s " + pattern, distance / 60, context.getString(R.string.short_hour), distance % 60, context.getString(R.string.short_minute));
        }
        return String.format(pattern, distance, context.getString(R.string.short_minute));
    }
}
