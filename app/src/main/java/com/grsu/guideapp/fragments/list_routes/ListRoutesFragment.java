package com.grsu.guideapp.fragments.list_routes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.delegation.NavigationDrawerActivity;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRoutesFragment extends BaseFragment<ListRoutesPresenter, NavigationDrawerActivity>
        implements ListRoutesViews, Callback<List<Datum>> {

    private static final String TAG = ListRoutesFragment.class.getSimpleName();

    RoutesListAdapter adapter;
    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView rw_fragment_list_routes;

    @BindView(R.id.cv_fragment_list_routes_load_more)
    CardView cv_fragment_list_routes_load_more;

    @Override
    public void onResume() {
        super.onResume();
        if (App.isOnline()) {
            cv_fragment_list_routes_load_more.setVisibility(View.VISIBLE);
        } else {
            cv_fragment_list_routes_load_more.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    protected ListRoutesPresenter getPresenterInstance() {
        return new ListRoutesPresenter(this,
                new ListRoutesInteractor(new DatabaseHelper(getContext())));
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
        mPresenter.getListRoutes(getString(R.string.locale));
        mPresenter.createDBIfNeed(getContext());
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

    @OnClick(R.id.cv_fragment_list_routes_load_more)
    public void loadMore() {
        if (App.isOnline()) {
            Call<List<Datum>> routes = App.getThread().networkIO().getRoutes(BuildConfig.ApiKey);
            routes.enqueue(this);
        } else {
            showToast("Нет интернет соединения");
        }
        cv_fragment_list_routes_load_more.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(@NonNull Call<List<Datum>> call,
            @NonNull Response<List<Datum>> response) {
        Log.e("TAG", "onResponse: ");
        if (response.isSuccessful()) {
            if (adapter != null) {
                new Test(getActivity).loadRoute(response.body(), adapter);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<Datum>> call, @NonNull Throwable t) {
        if (!call.isCanceled()) {
            Log.e("TAG", "onFailure: ", t);
        } else {
            Log.e("TAG", "onFailure: IS CANCELED");
        }
    }
}
