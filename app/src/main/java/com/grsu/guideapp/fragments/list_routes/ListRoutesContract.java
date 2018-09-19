package com.grsu.guideapp.fragments.list_routes;

import android.content.Context;
import android.support.annotation.StringRes;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.Route;
import java.util.List;

public interface ListRoutesContract extends OnSuccessListener<List<Route>> {

    interface ListRoutesViews extends BaseView {

        void showMessage(@StringRes int message);

        void showMessage(Throwable throwable);

        void setData(List<Route> routes);

        void initial();
    }

    interface ListRoutesPresenter extends BasePresenter<ListRoutesViews> {

        void createDBIfNeed(Context context);

        void getListRoutes();
    }

    interface ListRoutesInteractor {

        void getListAllRoutes(OnSuccessListener<List<Route>> listener);
    }
}
