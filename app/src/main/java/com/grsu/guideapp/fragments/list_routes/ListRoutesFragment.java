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
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.RoutesListAdapter;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Root;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRoutesFragment extends BaseFragment<ListRoutesPresenter, NavigationDrawerActivity>
        implements ListRoutesViews, Callback<Root>, OnFinishedListener<Integer>, OnRefreshListener {

    private static final String TAG = ListRoutesFragment.class.getSimpleName();
    private Test helper;

    RoutesListAdapter adapter;
    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView rw_fragment_list_routes;

    @BindView(R.id.srl_fragment_list_routes)
    SwipeRefreshLayout srl_fragment_list_routes;

    @Override
    public void onStart() {
        super.onStart();
        String locale = getString(R.string.locale);
        List<Route> routes = helper.getListRoutes(locale);
        if (adapter != null) {
            adapter.setRoutesList(routes);
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
        adapter = new RoutesListAdapter(getActivity, routes);
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
                List<Route> listRoutes = helper.getListRoutes(getString(R.string.locale));
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
}
