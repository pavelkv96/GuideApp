package com.grsu.guideapp.holders.routes;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BaseViewHolder;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.CryptoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.File;

public class BaseRouteViewHolder extends BaseViewHolder<Route> {

    @BindView(R.id.iv_item_preview_photo_route)
    protected ImageView iv_item_preview_photo_route;
    @BindView(R.id.tv_item_routes_duration)
    protected TextView tv_item_routes_duration;
    @BindView(R.id.tv_item_routes_distance)
    protected TextView tv_item_routes_distance;
    @BindView(R.id.tv_item_routes_name_route)
    protected TextView tv_item_routes_name_route;

    protected ItemClickListener listener;

    BaseRouteViewHolder(View itemView, ItemClickListener listener) {
        super(itemView, listener);
    }

    public void bind(Route route) {
        Context context = itemView.getContext();

        String duration;
        String distance;
        switch (route.getIdRoute()) {
            case 627: {
                distance = String.format("3.1 %s", context.getString(R.string.short_kilometers));
                duration = String.format("15 %s", context.getString(R.string.short_minute));
            }
            break;
            case 630: {
                distance = String.format("49.9 %s", context.getString(R.string.short_kilometers));
                duration = String.format("5 %s", context.getString(R.string.short_hour));
            }
            break;
            case 504: {
                distance = String.format("12 %s", context.getString(R.string.short_kilometers));
                duration = String.format("3 %s", context.getString(R.string.short_hour));
            }
            break;
            default: {
                duration = toDuration(route.getDuration(), context);
                distance = toDistance(route.getDistance(), context);
            }
        }

        tv_item_routes_duration.setText(duration);
        tv_item_routes_distance.setText(distance);
        tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute().getShortName()));

        String photo = CryptoUtils.hash(route.getReferencePhotoRoute());
        File file = new File(Settings.CONTENT, photo);
        Picasso picasso = Picasso.get();

        RequestCreator error = picasso.load(file)
                .placeholder(R.drawable.my_location)
                .error(R.drawable.ic_launcher_background);
        if (iv_item_preview_photo_route != null) {
            error.into(iv_item_preview_photo_route);
        }
    }

    private String toDistance(Integer distance, Context context) {
        String pattern = "%s %s";
        if (distance > 900) {
            return String
                    .format(pattern, distance / 1000, context.getString(R.string.short_kilometers));
        }
        return String.format(pattern, distance, context.getString(R.string.short_meters));
    }

    private String toDuration(Integer distance, Context context) {
        String pattern = "%s %s";
        if (distance > 60) {
            return String.format("%s %s " + pattern, distance / 60,
                    context.getString(R.string.short_hour), distance % 60,
                    context.getString(R.string.short_minute));
        }
        return String.format(pattern, distance, context.getString(R.string.short_minute));
    }

    public enum Const {
        UPDATE, DOWNLOAD, ABOUT, ITEM
    }
}
