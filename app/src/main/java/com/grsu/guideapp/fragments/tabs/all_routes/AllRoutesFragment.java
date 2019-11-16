package com.grsu.guideapp.fragments.tabs.all_routes;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.adapters.AllRoutesListAdapter;
import com.grsu.guideapp.base.BaseChildFragment;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.tabs.all_routes.AllRoutesContract.AllRoutesViews;
import com.grsu.guideapp.fragments.tabs.list_routes.ListRoutesFragment;
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

public class AllRoutesFragment extends BaseChildFragment<AllRoutesPresenter, ListRoutesFragment>
        implements AllRoutesViews, Callback<Root>, OnFinishedListener<Integer>, OnRefreshListener,
        ItemClickListener {

    private static final String TAG = AllRoutesFragment.class.getSimpleName();
    private Test helper;
    private List<Route> listRoutes;

    AllRoutesListAdapter adapter;
//    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView rw_fragment_list_routes;

//    @BindView(R.id.srl_fragment_all_routes)
    SwipeRefreshLayout srl_fragment_list_routes;

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getListRoutes(getString(R.string.locale));
        if (adapter != null) {
            adapter.setRoutesList(listRoutes);
        }
    }

    @NonNull
    @Override
    protected AllRoutesPresenter getPresenterInstance() {
        helper = new Test(getContext());
        return new AllRoutesPresenter(this, new AllRoutesInteractor(helper));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_all_routes;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        rw_fragment_list_routes.setHasFixedSize(true);
        rw_fragment_list_routes.setLayoutManager(new LinearLayoutManager(getContext()));
        srl_fragment_list_routes.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void showMessage(Throwable throwable) {
        Logs.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public void setData(final List<Route> routes) {
        listRoutes = routes;
        adapter = new AllRoutesListAdapter(listRoutes, this);
        rw_fragment_list_routes.setAdapter(adapter);
    }

    @Override
    public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
        Log.e("TAG", "onResponse: ");
        if (response.isSuccessful()) {
            if (response.body() != null && adapter != null) {
                List<Datum> datums = response.body().getDatums();
                helper.loadRoute(this, datums);
            }
        } else {
            showToast(response.message());
            srl_fragment_list_routes.setRefreshing(false);
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
                if (srl_fragment_list_routes != null) {
                    srl_fragment_list_routes.setRefreshing(false);
                }
                showToast(integer);
                listRoutes = helper.getListRoutes(getString(R.string.locale));
                if (adapter!=null)
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
        if (CheckPermission.checkStoragePermission(getActivity()) && listRoutes != null) {
            startActivity(RouteActivity.newIntent(getActivity(), listRoutes.get(position)));
        } else {
            int message = R.string.error_snackbar_do_not_have_permission_write_on_the_storage;
            MySnackbar.makeL(getView(), message, getActivity());
        }
    }

    public static AllRoutesFragment newInstance() {
        return new AllRoutesFragment();
    }
}
