package com.grsu.guideapp.fragments.list_routes;

import android.content.Context;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.Route;
import java.util.List;

public interface ListRoutesContract extends OnSuccessListener<List<Route>> {

    interface ListRoutesViews extends BaseView {

        void showMessage(Throwable throwable);

        void setData(List<Route> routes);
    }

    interface ListRoutesPresenter extends BasePresenter<ListRoutesViews> {

        void createDBIfNeed(Context context);

        void getListRoutes(String locale);
    }

    interface ListRoutesInteractor {

        void getListAllRoutes(OnSuccessListener<List<Route>> listener, String locale);
    }
}
