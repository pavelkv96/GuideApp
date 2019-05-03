package com.grsu.guideapp.fragments.list_routes;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesInteractor;
import com.grsu.guideapp.fragments.list_routes.ListRoutesContract.ListRoutesViews;
import com.grsu.guideapp.models.Route;
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
    public void getListRoutes(String locale) {
        listRoutesInteractor.getListAllRoutes(this, locale);
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
