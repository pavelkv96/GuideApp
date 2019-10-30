package com.grsu.guideapp.fragments.tabs.my_routes;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.Route;
import java.util.List;

interface MyRoutesContract {

    interface MyRoutesView extends BaseView {
        void updateList(List<Route> routes);
    }

    interface MyRoutesPresenter extends BasePresenter<MyRoutesView> {
        void getListRoute(String locale);
    }

    interface MyRoutesInteractor {
        void getListRoute(OnSuccessListener<List<Route>> listener, String locale);
    }
}
