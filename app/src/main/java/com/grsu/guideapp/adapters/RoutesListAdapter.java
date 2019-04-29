package com.grsu.guideapp.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.holders.RouteViewHolder;
import com.grsu.guideapp.models.Route;
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
        final Route route = routesList.get(position);
        holder.bind(route, activity, this);
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    public void setRoutesList(List<Route> routesList) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("load", true).apply();
        this.routesList = routesList;
        notifyDataSetChanged();
    }
}
