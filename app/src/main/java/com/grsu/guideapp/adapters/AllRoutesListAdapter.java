package com.grsu.guideapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.holders.routes.RoutesViewHolder;
import com.grsu.guideapp.models.Route;
import java.util.List;

public class AllRoutesListAdapter extends Adapter<RoutesViewHolder> {

    private List<Route> routesList;
    private ItemClickListener listener;

    public AllRoutesListAdapter(List<Route> mRoutesList, ItemClickListener listener) {
        this.routesList = mRoutesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        View rootView = from.inflate(R.layout.item_routes_downloaded, parent, false);
        return new RoutesViewHolder(rootView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesViewHolder holder, int position) {
        Route route = routesList.get(position);
        holder.bind(route);
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    public void setRoutesList(List<Route> routesList) {
        this.routesList = routesList;
        notifyDataSetChanged();
    }
}
