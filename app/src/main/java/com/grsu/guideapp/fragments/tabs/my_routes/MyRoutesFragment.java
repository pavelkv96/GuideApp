package com.grsu.guideapp.fragments.tabs.my_routes;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.route.RouteActivity;
import com.grsu.guideapp.adapters.AllRoutesListAdapter;
import com.grsu.guideapp.base.BaseChildFragment;
import com.grsu.guideapp.base.listeners.ItemClickListener;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.tabs.my_routes.MyRoutesContract.MyRoutesView;
import com.grsu.guideapp.fragments.tabs.list_routes.ListRoutesFragment;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.CheckPermission;
import com.grsu.guideapp.utils.MessageViewer.MySnackbar;
import java.util.List;

public class MyRoutesFragment
        extends BaseChildFragment<MyRoutesPresenter, ListRoutesFragment>
        implements MyRoutesView, ItemClickListener {

    private List<Route> mRouteList;

//    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView mRecyclerView;

    private AllRoutesListAdapter mAdapter;

    @NonNull
    @Override
    protected MyRoutesPresenter getPresenterInstance() {
        return new MyRoutesPresenter(new MyRoutesInteractor(new Test(getContext())));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_routes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new AllRoutesListAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getListRoute(getString(R.string.locale));
    }

    @Override
    public void updateList(List<Route> routes) {
        mRouteList = routes;
        if (mAdapter != null) {
            mAdapter.setRoutesList(routes);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (CheckPermission.checkStoragePermission(getActivity()) && mRouteList != null) {
            startActivity(RouteActivity.newIntent(getActivity(), mRouteList.get(position)));
        } else {
            int message = R.string.error_snackbar_do_not_have_permission_write_on_the_storage;
            MySnackbar.makeL(getView(), message, getActivity());
        }
    }

    public static MyRoutesFragment newInstance() {
        return new MyRoutesFragment();
    }
}