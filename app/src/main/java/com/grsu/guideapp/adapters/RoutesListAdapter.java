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
        Route route = routesList.get(position);
        final Integer id_route = route.getIdRoute();
        final String name_route = route.getNameRoute();
        holder.bind(route);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(view, id_route, name_route);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routesList != null ? routesList.size() : 0;
    }

    private void openActivity(View view, Integer id_route, String name_route) {
        if (CheckSelfPermission.writeExternalStorageIsGranted(activity)) {
            MySnackbar.makeL(view,
                    R.string.error_snackbar_do_not_have_permission_write_on_the_storage, activity);
        } else {
            activity.startActivity(RouteActivity.newIntent(activity, id_route, name_route));
        }
    }
}
