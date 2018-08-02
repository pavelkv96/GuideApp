package com.grsu.guideapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.RoutesListAdapter.MyViewHolder;
import com.grsu.guideapp.models.Routes;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<MyViewHolder> {


    private List<Routes> routesList;

    public RoutesListAdapter(List<Routes> routesList) {
        this.routesList = routesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_type_item_activity_found_routes
                .setText(String.valueOf(routesList.get(position).getType()));
        holder.tv_distance_item_activity_found_routes
                .setText(String.valueOf(routesList.get(position).getDistance()));
        holder.tv_name_route_item_activity_found_routes
                .setText(String.valueOf(routesList.get(position).getNameRoute()));

        Picasso.get().load(routesList.get(position).getReferencePhotoRoute())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.iv_item_activity_found_routes);
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    class MyViewHolder extends ViewHolder {

        ImageView iv_item_activity_found_routes;
        TextView tv_type_item_activity_found_routes;
        TextView tv_distance_item_activity_found_routes;
        TextView tv_name_route_item_activity_found_routes;
        //TextView tv_author_item_activity_found_routes;
        View mView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            iv_item_activity_found_routes = mView
                    .findViewById(R.id.iv_item_activity_found_routes);
            tv_type_item_activity_found_routes = mView
                    .findViewById(R.id.tv_type_item_activity_found_routes);
            tv_distance_item_activity_found_routes = mView
                    .findViewById(R.id.tv_distance_item_activity_found_routes);
            tv_name_route_item_activity_found_routes = mView
                    .findViewById(R.id.tv_name_route_item_activity_found_routes);
            /*tv_author_item_activity_found_routes = mView
                    .findViewById(R.id.tv_author_item_activity_found_routes);*/
        }
    }
}
