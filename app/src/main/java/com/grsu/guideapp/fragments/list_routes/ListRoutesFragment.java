package com.grsu.guideapp.fragments.list_routes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.OnClick;
import com.grsu.guideapp.App;
import com.grsu.guideapp.AppExecutors;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.adapters.RoutesListAdapter;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Table;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.holders.routes.BaseRouteViewHolder.Const;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Root;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRoutesFragment extends BaseFragment<ListRoutesPresenter, NavigationDrawerActivity>
        implements ListRoutesViews, Callback<Root>, OnFinishedListener<Integer>, OnRefreshListener,
        ItemClickListener {

    private static final String TAG = ListRoutesFragment.class.getSimpleName();
    private Test helper;
    private List<Route> listRoutes;

    RoutesListAdapter adapter;
    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView rw_fragment_list_routes;

    @BindView(R.id.srl_fragment_list_routes)
    SwipeRefreshLayout srl_fragment_list_routes;

    @Override
    public void onStart() {
        super.onStart();
        String locale = getString(R.string.locale);
        listRoutes = helper.getListRoutes(locale);
        if (adapter != null) {
            adapter.setRoutesList(listRoutes);
        }
    }

    @NonNull
    @Override
    protected ListRoutesPresenter getPresenterInstance() {
        helper = new Test(getContext());
        return new ListRoutesPresenter(this, new ListRoutesInteractor(helper));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_routes;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.list_routes_fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity.setTitleToolbar(getTitle());
        rw_fragment_list_routes.setHasFixedSize(true);
        rw_fragment_list_routes.setLayoutManager(new LinearLayoutManager(getContext()));
        srl_fragment_list_routes.setOnRefreshListener(this);
        mPresenter.getListRoutes(getString(R.string.locale));
        return view;
    }

    @Override
    public void showMessage(Throwable throwable) {
        Logs.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public void setData(final List<Route> routes) {
        listRoutes = routes;
        adapter = new RoutesListAdapter(listRoutes, this);
        rw_fragment_list_routes.setAdapter(adapter);
    }

    @OnClick(R.id.tv_fragment_list_routes_about)
    public void onClick(View view) {
        getActivity.openAboutFragment();
    }

    @Override
    public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
        Log.e("TAG", "onResponse: ");
        if (response.isSuccessful()) {
            if (response.body() != null && adapter != null) {
                List<Datum> datums = response.body().getDatums();
                helper.loadRoute(this, datums);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
        if (!call.isCanceled()) {
            Log.e("TAG", "onFailure: ", t);
        } else {
            Log.e("TAG", "onFailure: IS CANCELED");
        }
        srl_fragment_list_routes.setRefreshing(false);
    }

    @Override
    public void onFinished(final Integer integer) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                srl_fragment_list_routes.setRefreshing(false);
                showToast(integer);
                listRoutes = helper.getListRoutes(getString(R.string.locale));
                adapter.setRoutesList(listRoutes);
            }
        });
    }

    @Override
    public void onRefresh() {
        srl_fragment_list_routes.setRefreshing(true);
        if (App.isOnline()) {
            Call<Root> root = App.getThread().networkIO().getRoutes(BuildConfig.ApiKey);
            root.enqueue(this);
        } else {
            srl_fragment_list_routes.setRefreshing(false);
            showToast(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view != null && view.getTag() instanceof Const) {

            Const tag = (Const) view.getTag();
            switch (tag) {
                case ITEM:
                case ABOUT: {
                    about(position);
                }
                break;
                case UPDATE: {
                    update(position);
                }
                break;
                case DOWNLOAD: {
                    download(position);
                }
                break;
                default:
                    throw new IllegalArgumentException("Not support tag: " + view.getTag());
            }
            Log.e(TAG, "onItemClick: " + position + " " + view.getTag());
        }
    }

    private void update(int position) {
        final Route route = listRoutes.get(position);
        final Test test = new Test(getActivity);
        showProgress("", getString(R.string.updating_route));
        test.updateFullRouteById(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "onSuccess: ");
                App.getThread().mainThread(new Runnable() {
                    @Override
                    public void run() {
                        String locale = getString(R.string.locale);
                        listRoutes = test.getListRoutes(locale);
                        adapter.setRoutesList(listRoutes);
                        hideProgress();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage(), throwable);
            }}, route.getIdRoute());
    }

    private void download(int position) {
        final Route route = listRoutes.get(position);
        showProgress("", getString(R.string.download_route));
        final AppExecutors thread = App.getThread();
        thread.diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    final Test helper = new Test(getActivity);
                    List<Integer> poiFromBD = helper.getListPoiFromBD(route.getIdRoute());

                    for (Integer id : poiFromBD) {
                        Response<Datum> datum = thread.networkIO().getPoi(id, BuildConfig.ApiKey).execute();
                        if (datum.isSuccessful()) {
                            helper.insertPoiAndTypes(datum.body());
                        }

                    }
                    helper.setDownload(route.getIdRoute(), Table.DOWNLOAD);
                    listRoutes = helper.getListRoutes(getString(R.string.locale));
                    thread.mainThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setRoutesList(listRoutes);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                } finally {
                    thread.mainThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                        }
                    });
                }
            }
        });
    }

    private void about(int position) {
        if (CheckPermission.checkStoragePermission(getActivity) && listRoutes != null) {
            startActivity(RouteActivity.newIntent(getActivity, listRoutes.get(position)));
        } else {
            int message = R.string.error_snackbar_do_not_have_permission_write_on_the_storage;
            MySnackbar.makeL(getView(), message, getActivity);
        }
    }
}
