package com.grsu.guideapp.fragments.tabs.all_routes;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.Route;
import java.util.List;

interface AllRoutesContract extends OnSuccessListener<List<Route>> {

    interface AllRoutesViews extends BaseView {

        void showMessage(Throwable throwable);

        void setData(List<Route> routes);
    }

    interface AllRoutesPresenter extends BasePresenter<AllRoutesViews> {

        void getListRoutes(String locale);
    }

    interface AllRoutesInteractor {

        void getListAllRoutes(OnSuccessListener<List<Route>> listener, String locale);
    }
}
