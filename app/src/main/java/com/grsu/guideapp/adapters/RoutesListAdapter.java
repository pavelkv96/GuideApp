package com.grsu.guideapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.database.Table;
import com.grsu.guideapp.holders.routes.BaseRouteViewHolder;
import com.grsu.guideapp.holders.routes.DownloadViewHolder;
import com.grsu.guideapp.holders.routes.UpdateViewHolder;
import com.grsu.guideapp.holders.routes.NotDownloadViewHolder;
import com.grsu.guideapp.models.Route;
import java.util.List;

public class RoutesListAdapter extends RecyclerView.Adapter<BaseRouteViewHolder> {

    private List<Route> routesList;
    private ItemClickListener listener;

    public RoutesListAdapter(List<Route> mRoutesList, ItemClickListener listener) {
        this.routesList = mRoutesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseRouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        return getViewByType(from, parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRouteViewHolder holder, int position) {
        Route route = routesList.get(position);
        holder.bind(route);
    }

    @Override
    public int getItemViewType(int position) {
        return routesList.get(position).getIsFull();
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    public void setRoutesList(List<Route> routesList) {
        this.routesList = routesList;
        notifyDataSetChanged();
    }

    private BaseRouteViewHolder getViewByType(LayoutInflater from, ViewGroup parent, int type) {
        switch (type) {
            case Table.NOT_DOWNLOAD: {
                View rootView = from.inflate(R.layout.item_routes_not_download, parent, false);
                return new NotDownloadViewHolder(rootView, listener);
            }
            case Table.HAVE_UPDATE: {
                View rootView = from.inflate(R.layout.item_routes_have_update, parent, false);
                return new UpdateViewHolder(rootView, listener);
            }
            case Table.DOWNLOAD: {
                View rootView = from.inflate(R.layout.item_routes_downloaded, parent, false);
                return new DownloadViewHolder(rootView, listener);
            }
            default:
                throw new IllegalArgumentException("Invalid type " + type);
        }
    }
}
