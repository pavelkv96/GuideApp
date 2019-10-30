package com.grsu.guideapp.fragments.tabs.all_routes;

import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.fragments.tabs.all_routes.AllRoutesContract.AllRoutesInteractor;
import com.grsu.guideapp.fragments.tabs.all_routes.AllRoutesContract.AllRoutesViews;
import com.grsu.guideapp.models.Route;
import java.util.List;

public class AllRoutesPresenter extends BasePresenterImpl<AllRoutesViews> implements
        AllRoutesContract.AllRoutesPresenter, OnSuccessListener<List<Route>> {

    private AllRoutesViews mViews;
    private AllRoutesInteractor mInteractor;

    AllRoutesPresenter(AllRoutesViews pViews, AllRoutesInteractor pInteractor) {
        this.mViews = pViews;
        this.mInteractor = pInteractor;
    }

    @Override
    public void getListRoutes(String locale) {
        mInteractor.getListAllRoutes(this, locale);
    }

    @Override
    public void onSuccess(List<Route> routes) {
        mViews.setData(routes);
    }

    @Override
    public void onFailure(Throwable throwable) {
        mViews.showMessage(throwable);
    }
}
