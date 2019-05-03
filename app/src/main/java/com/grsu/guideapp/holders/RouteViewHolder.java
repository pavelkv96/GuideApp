package com.grsu.guideapp.holders;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.adapters.RoutesListAdapter;
import com.grsu.guideapp.database.Table;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;
import retrofit2.Response;

public class RouteViewHolder extends ViewHolder {

    private static final String TAG = RouteViewHolder.class.getSimpleName();
    private ImageView iv_item_preview_photo_route;
    private TextView tv_item_routes_duration;
    private TextView tv_item_routes_distance;
    private TextView tv_item_routes_name_route;
    private Button btn_item_routes_about;
    private Button btn_item_routes_download;
    private ImageButton ib_item_routes_download;

    public RouteViewHolder(@NonNull View pView) {
        super(pView);

        iv_item_preview_photo_route = pView.findViewById(R.id.iv_item_preview_photo_route);
        tv_item_routes_duration = pView.findViewById(R.id.tv_item_routes_duration);
        tv_item_routes_distance = pView.findViewById(R.id.tv_item_routes_distance);
        tv_item_routes_name_route = pView.findViewById(R.id.tv_item_routes_name_route);
        btn_item_routes_about = pView.findViewById(R.id.btn_item_routes_about);
        btn_item_routes_download = pView.findViewById(R.id.btn_item_routes_download);
        ib_item_routes_download = pView.findViewById(R.id.ib_item_routes_download);
    }

    public void bind(final Route route, final Context context, final RoutesListAdapter adapter) {

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
        tv_item_routes_name_route.setText(String.valueOf(route.getNameRoute()));

        String photo = CryptoUtils.hash(route.getReferencePhotoRoute());
        File file = new File(Settings.CONTENT, photo);

        updateView(route, context, adapter);

        Picasso.get().load(file)
                .placeholder(R.drawable.my_location)
                .error(R.drawable.ic_launcher_background)
                .into(iv_item_preview_photo_route);
    }

    private void openActivity(View view, Route route, Context activity) {
        if (CheckPermission.canWriteStorage(activity)) {
            activity.startActivity(RouteActivity.newIntent(activity, route));
        } else {
            int message = R.string.error_snackbar_do_not_have_permission_write_on_the_storage;
            MySnackbar.makeL(view, message, activity);
        }
    }

    private void updateView(final Route route, final Context context,
            final RoutesListAdapter adapter) {
        switch (route.getIsFull()) {
            case Table.NOT_DOWNLOAD: {

                btn_item_routes_about.setVisibility(View.VISIBLE);
                btn_item_routes_download.setVisibility(View.VISIBLE);

                btn_item_routes_about.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity(view, route, context);
                    }
                });

                btn_item_routes_download.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final NavigationDrawerActivity activity = (NavigationDrawerActivity) context;
                        activity.showProgress("", context.getString(R.string.download_route));
                        App.getThread().diskIO(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Test helper = new Test(context);
                                    List<Integer> poiFromBD = helper
                                            .getListPoiFromBD(route.getIdRoute());

                                    for (Integer id : poiFromBD) {
                                        Response<Datum> datum = App.getThread().networkIO()
                                                .getPoi(id, BuildConfig.ApiKey).execute();
                                        if (datum.isSuccessful()) {
                                            new Test(context).insertPoiAndTypes(datum.body());
                                        }

                                    }
                                    helper.setDownload(route.getIdRoute(), Table.DOWNLOAD);
                                    final List<Route> routes = helper
                                            .getListRoutes(context.getString(R.string.locale));
                                    App.getThread().mainThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            PreferenceManager.getDefaultSharedPreferences(context)
                                                    .edit().putBoolean(
                                                    SharedPref.KEY_LOAD, true).apply();
                                            adapter.setRoutesList(routes);
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e(TAG, "onFailure: ", e);
                                } finally {
                                    App.getThread().mainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.hideProgress();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
            break;
            case Table.HAVE_UPDATE: {
                ib_item_routes_download.setVisibility(View.VISIBLE);
            }
            break;
            case Table.DOWNLOAD: {
                btn_item_routes_about.setVisibility(View.GONE);
                btn_item_routes_download.setVisibility(View.GONE);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity(view, route, context);
                    }
                });
            }
            break;
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
}
