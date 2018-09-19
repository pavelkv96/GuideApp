package com.grsu.guideapp.fragments.list_routes;

import android.content.Context;
import com.grsu.guideapp.R;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.DatabaseHelper;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesInteractor;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.models.Route;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.StorageUtils;
import java.io.File;
import java.util.List;

public class ListRoutesPresenter extends BasePresenterImpl<ListRoutesViews> implements
        ListRoutesContract.ListRoutesPresenter, OnSuccessListener<List<Route>> {

    private static final String TAG = ListRoutesPresenter.class.getSimpleName();
    private ListRoutesViews listRoutesViews;
    private ListRoutesInteractor listRoutesInteractor;

    public ListRoutesPresenter(ListRoutesViews listRoutesViews,
            ListRoutesInteractor listRoutesInteractor) {
        this.listRoutesViews = listRoutesViews;
        this.listRoutesInteractor = listRoutesInteractor;
    }

    @Override
    public void createDBIfNeed(Context context) {
        File database = context.getDatabasePath(Settings.DATABASE_INFORMATION_NAME);
        if (!database.exists()) {
            new DatabaseHelper(context);
            if (StorageUtils.copyDatabase(context)) {
                listRoutesViews.showMessage(R.string.success_copy_database_success);
                listRoutesViews.initial();
            } else {
                listRoutesViews.showMessage(R.string.error_copy_data_error);
            }
        }
    }

    @Override
    public void getListRoutes() {
        listRoutesInteractor.getListAllRoutes(this);
    }

    @Override
    public void onSuccess(List<Route> routes) {
        listRoutesViews.setData(routes);
    }

    @Override
    public void onFailure(Throwable throwable) {
        listRoutesViews.showMessage(throwable);
    }
}
