package com.grsu.guideapp.fragments.list_routes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.adapters.RoutesListAdapter;
import com.grsu.guideapp.base.BaseFragment;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import java.util.List;

public class ListRoutesFragment extends BaseFragment<ListRoutesPresenter> implements
        ListRoutesViews {

    private static final String TAG = ListRoutesFragment.class.getSimpleName();

    @BindView(R.id.rv_fragment_list_routes)
    RecyclerView rw_fragment_list_routes;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.createDBIfNeed(getContext());
    }

    @Override
    public void showMessage(int message) {
        Toasts.makeS(getContext(), getString(message));
    }

    @Override
    public void showMessage(Throwable throwable) {
        Logs.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public void setData(final List<Route> routes) {
        rw_fragment_list_routes.setAdapter(new RoutesListAdapter(getActivity(), routes));
    }

    @Override
    public void initial() {
        rw_fragment_list_routes.setHasFixedSize(true);
        rw_fragment_list_routes.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.getListRoutes();
    }
}
