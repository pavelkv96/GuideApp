package com.grsu.guideapp.fragments.route_preview;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.map.MapFragment;
import com.grsu.guideapp.fragments.open_route.OpenRouteFragment;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.RoutePresenter;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.RouteViews;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.models.Names;
import com.grsu.guideapp.project_settings.Constants;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import com.squareup.picasso.Picasso;

public class RoutePreviewFragment extends BaseFragment<RoutePresenter, RouteActivity>
        implements RouteViews {

//    @BindView(R.id.tv_fragment_route_preview_description)
    TextView description;

//    @BindView(R.id.tv_fragment_route_preview_distance)
    TextView distance;

//    @BindView(R.id.tv_fragment_route_preview_duration)
    TextView duration;

//    @BindView(R.id.iv_fragment_route_preview_image)
    ImageView image;

//    @BindView(R.id.btn_fragment_route_preview_start)
    Button startRoute;

//    @BindView(R.id.btn_fragment_route_preview_map)
    Button openPreview;

//    @BindView(R.id.btn_fragment_route_preview_download)
    Button downloadRoute;

//    @BindView(R.id.btn_fragment_route_preview_update)
    Button updateRoute;

    private int id = -1;
    private Bundle mBundle;

    @NonNull
    @Override
    protected RoutePresenter getPresenterInstance() {
        return new RoutePreviewPresenter(new RoutePreviewInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_route_preview;
    }

    @Override
    protected String getTitle() {
        return "Error name";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getTitle();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mBundle = bundle;
            id = bundle.getInt(Constants.KEY_ID_ROUTE);
            String title = bundle.getString(Constants.KEY_NAME_ROUTE, getTitle());
            getActivity.setTitleToolbar(title);
            mPresenter.getRouteById(id, getString(R.string.locale));
        } else {
            closeFragment();
        }

        return rootView;
    }

    public static RoutePreviewFragment newInstance(Bundle args) {
        RoutePreviewFragment fragment = new RoutePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setData(DtoRoute route) {
        Names names = route.getNameRoute();
        if (!names.getDescription().trim().isEmpty()) {
            description.setText(names.getDescription());
        }

        Pair<String, String> pair = set(route);

        distance.setText(pair.first);
        duration.setText(pair.second);

        startRoute.setVisibility(route.getIsFull() == 0 ? View.GONE : View.VISIBLE);
        openPreview.setVisibility(route.getIsFull() == 0 ? View.VISIBLE : View.GONE);
        updateRoute.setVisibility(route.getIsFull() == 1 ? View.VISIBLE : View.GONE);
        downloadRoute.setVisibility(route.getIsFull() == 0 ? View.VISIBLE : View.GONE);

        Picasso.get().load(route.getPhotoPath()).into(image);
    }

//    @OnClick(R.id.btn_fragment_route_preview_download)
    public void onClickDownload() {
        mPresenter.downloadRoute(id);
    }

//    @OnClick(R.id.btn_fragment_route_preview_map)
    public void onClickOpenMap(View view) {
        if (id != -1) {
            if (CheckPermission.INSTANCE.checkLocationPermission(getActivity)){
                mPresenter.openPreviewRoute(id);
            }else {
                MySnackbar.makeL(getView(), R.string.error_snackbar_do_not_have_permission_access_location, getActivity);
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

//    @OnClick(R.id.btn_fragment_route_preview_start)
    public void onClickStartRoute(View view) {
        if (id != -1) {
            if (CheckPermission.INSTANCE.checkLocationPermission(getActivity) && mBundle != null){
                mPresenter.openRoute(mBundle);
            }else {
                MySnackbar.makeL(getView(), R.string.error_snackbar_do_not_have_permission_access_location, getActivity);
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

//    @OnClick(R.id.btn_fragment_route_preview_update)
    public void onClickUpdateRoute() {
        if (CheckPermission.INSTANCE.checkStoragePermission(getActivity)) {
            mPresenter.updateRoute(id);
        }
    }

    @Override
    public void showProgress(String title, String message) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(getActivity);
            if (title != null) {
                mProgressDialog.setTitle(title);
            }
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    public void closeFragment() {
        getActivity.getSupportFragmentManager().popBackStack();
        getActivity.finish();
    }

    @Override
    public void openFragment(Object data) {
        if (data instanceof Bundle) {
            getActivity.onReplace(MapFragment.newInstance((Bundle) data));
        }
        if (data instanceof Integer) {
            //showToast("Ещё в разработке");
            getActivity.onReplace(OpenRouteFragment.newInstance((Integer)data));
        }
    }

    @Override
    public void visibleStartRouteButton(boolean isVisible) {
        startRoute.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void visibleDownloadRouteButton(boolean isVisible) {
        downloadRoute.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void visibleUpdateRouteButton(boolean isVisible) {
        updateRoute.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void visiblePreviewRouteButton(boolean isVisible) {
        openPreview.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private Pair<String, String> set(DtoRoute route) {
        String duration;
        String distance;
        switch (route.getIdRoute()) {
            case 502: {
                distance = String.format("%s: 72,7 %s", getString(R.string.distance),
                        getString(R.string.short_kilometers));
                duration = String.format("%s: 10 %s", getString(R.string.duration),
                        getString(R.string.short_hour));
            }
            break;
            case 630: {
                distance = String.format("%s: 49.9 %s", getString(R.string.distance),
                        getString(R.string.short_kilometers));
                duration = String.format("%s: 5 %s", getString(R.string.duration),
                        getString(R.string.short_hour));
            }
            break;
            case 762: {
                distance = String.format("%s: 12 %s", getString(R.string.distance),
                        getString(R.string.short_kilometers));
                duration = String.format("%s: 3 %s", getString(R.string.duration),
                        getString(R.string.short_hour));
            }
            break;
            default: {
                distance = toDistance(route.getDistance());
                duration = toDuration(route.getDuration());
            }
        }
        return new Pair<>(distance, duration);
    }


    private String toDistance(Integer distance) {
        String pattern = "%s: %s %s";
        if (distance > 900) {
            return String.format(pattern, getString(R.string.distance), distance / 1000,
                    getString(R.string.short_kilometers));
        }
        return String.format(pattern, getString(R.string.distance), distance,
                getString(R.string.short_meters));
    }

    private String toDuration(Integer distance) {
        String pattern = "%s: %s %s %s %s";
        if (distance > 60) {
            return String.format(pattern, getString(R.string.duration), distance / 60,
                    getString(R.string.short_hour), distance % 60,
                    getString(R.string.short_minute));
        }
        return String.format(pattern, getString(R.string.duration), distance,
                getString(R.string.short_minute), "", "");
    }
}
