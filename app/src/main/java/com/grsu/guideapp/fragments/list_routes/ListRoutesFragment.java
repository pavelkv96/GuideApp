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
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.grsu.guideapp.network.model.Root;
import com.grsu.guideapp.project_settings.SharedPref;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRoutesFragment extends BaseFragment<ListRoutesPresenter, NavigationDrawerActivity>
        implements ListRoutesViews, Callback<Root>, OnFinishedListener<Integer> {

    private static final String TAG = ListRoutesFragment.class.getSimpleName();
    private Test helper;

    RoutesListAdapter adapter;
    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView rw_fragment_list_routes;

    @BindView(R.id.cv_fragment_list_routes_load_more)
    CardView cv_fragment_list_routes_load_more;

    @BindView(R.id.pb_fragment_list_routes_progress)
    ProgressBar pb_fragment_list_routes_progress;

    @BindView(R.id.tv_fragment_list_routes_more)
    TextView tv_fragment_list_routes_more;

    @Override
    public void onStart() {
        super.onStart();
        String locale = getString(R.string.locale);
        List<Route> routes = helper.getListRoutes(locale);
        if (adapter != null) {
            adapter.setRoutesList(routes);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.isOnline() && !contains(SharedPref.KEY_LOAD)) {
            tv_fragment_list_routes_more.setVisibility(View.VISIBLE);
            cv_fragment_list_routes_load_more.setVisibility(View.VISIBLE);
        } else {
            tv_fragment_list_routes_more.setVisibility(View.GONE);
            cv_fragment_list_routes_load_more.setVisibility(View.GONE);
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

    @OnClick(R.id.cv_fragment_list_routes_load_more)
    public void loadMore() {
        if (App.isOnline()) {
            Call<Root> root = App.getThread().networkIO().getRoutes(BuildConfig.ApiKey);
            root.enqueue(this);
            tv_fragment_list_routes_more.setVisibility(View.GONE);
            pb_fragment_list_routes_progress.setVisibility(View.VISIBLE);
        } else {
            showToast(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
        Log.e("TAG", "onResponse: ");
        if (response.isSuccessful()) {
            if (response.body() != null && adapter != null) {
                helper.loadRoute(this, response.body().getDatums());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
        pb_fragment_list_routes_progress.setVisibility(View.GONE);
        cv_fragment_list_routes_load_more.setVisibility(View.GONE);
        if (!call.isCanceled()) {
            Log.e("TAG", "onFailure: ", t);
        } else {
            Log.e("TAG", "onFailure: IS CANCELED");
        }
        pb_fragment_list_routes_progress.setVisibility(View.GONE);
        cv_fragment_list_routes_load_more.setVisibility(View.GONE);
    }

    @Override
    public void onFinished(final Integer integer) {
        App.getThread().mainThread(new Runnable() {
            @Override
            public void run() {
                showToast(integer);
                List<Route> listRoutes = helper.getListRoutes(getString(R.string.locale));
                save(SharedPref.KEY_LOAD, true);
                pb_fragment_list_routes_progress.setVisibility(View.GONE);
                cv_fragment_list_routes_load_more.setVisibility(View.GONE);
                adapter.setRoutesList(listRoutes);
            }
        });
    }
}
