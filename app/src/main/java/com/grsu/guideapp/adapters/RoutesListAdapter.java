package com.grsu.guideapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.adapters.RoutesListAdapter.RouteViewHolder;
import com.grsu.guideapp.models.Route;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<RouteViewHolder> {

    private List<Route> routesList;
    private FragmentActivity activity;

    public RoutesListAdapter(FragmentActivity activity, List<Route> mRoutesList) {
        this.activity = activity;
        this.routesList = mRoutesList;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_routes, parent, false);
        return new RouteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routesList.get(position);
        final Integer id_route = route.getIdRoute();

        holder.tv_item_routes_id_author.setText(String.valueOf(route.getIdAuthor()));
        holder.tv_item_routes_distance.setText(String.valueOf(route.getDistance()));
        holder.tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute()));

        Picasso.get().load(route.getReferencePhotoRoute())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.iv_item_preview_photo_route);

        holder.mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(RouteActivity.newIntent(activity, id_route));
            }
        });
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    class RouteViewHolder extends ViewHolder {

        ImageView iv_item_preview_photo_route;
        TextView tv_item_routes_id_author;
        TextView tv_item_routes_distance;
        TextView tv_item_routes_name_route;
        View mView;

        RouteViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            iv_item_preview_photo_route = mView
                    .findViewById(R.id.iv_item_preview_photo_route);
            tv_item_routes_id_author = mView
                    .findViewById(R.id.tv_item_routes_id_author);
            tv_item_routes_distance = mView
                    .findViewById(R.id.tv_item_routes_distance);
            tv_item_routes_name_route = mView
                    .findViewById(R.id.tv_item_routes_name_route);
        }
    }
}
