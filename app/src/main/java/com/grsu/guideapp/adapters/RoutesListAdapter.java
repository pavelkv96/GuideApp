package com.grsu.guideapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.RouteActivity;
import com.grsu.guideapp.adapters.RoutesListAdapter.RouteViewHolder;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.Constants;
import com.grsu.guideapp.utils.ContextHolder;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<RouteViewHolder> {

    private List<Route> routesList;

    public RoutesListAdapter(List<Route> routesList) {
        this.routesList = routesList;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routes, parent, false);
        return new RouteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        final Integer id_route = routesList.get(position).getIdRoute();
        holder.tv_item_routes_id_author
                .setText(String.valueOf(routesList.get(position).getIdAuthor()));
        holder.tv_item_routes_distance
                .setText(String.valueOf(routesList.get(position).getDistance()));
        holder.tv_item_routes_name_route
                .setText(String.valueOf(routesList.get(position).getNameRoute()));

        Picasso.get().load(routesList.get(position).getReferencePhotoRoute())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.iv_item_preview_photo_route);

        holder.mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ROUTE, id_route);
                ContextHolder.getContext().startActivity(
                        new Intent(ContextHolder.getContext(), RouteActivity.class)
                                .putExtras(bundle));
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
