package com.grsu.guideapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.holders.RouteViewHolder;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.CheckSelfPermission;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
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
        holder.bind(route);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(view, route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    private void openActivity(View view, Route route) {
        if (CheckSelfPermission.writeExternalStorageIsGranted(activity)) {
            int message = R.string.error_snackbar_do_not_have_permission_write_on_the_storage;
            MySnackbar.makeL(view, message, activity);
        } else {
            activity.startActivity(RouteActivity.newIntent(activity, route));
        }
    }
}
