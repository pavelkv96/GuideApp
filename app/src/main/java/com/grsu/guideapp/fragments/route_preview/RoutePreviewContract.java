package com.grsu.guideapp.fragments.route_preview;

import android.os.Bundle;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnLoadRoute;
import com.grsu.guideapp.base.listeners.OnProgressListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.DtoRoute;

interface RoutePreviewContract {

    interface RouteViews extends BaseView {

        void setData(DtoRoute route);

        void closeFragment();

        void openFragment(Object data);

        void visibleStartRouteButton(boolean isVisible);

        void visibleDownloadRouteButton(boolean isVisible);

        void visibleUpdateRouteButton(boolean isVisible);

        void visiblePreviewRouteButton(boolean isVisible);
    }

    interface RoutePresenter extends BasePresenter<RouteViews> {

        void getRouteById(int id, String locale);

        void downloadRoute(int id);

        void updateRoute(int id);

        void openPreviewRoute(int id);

        void openRoute(Bundle bundle);
    }

    interface RouteInteractor {

        void getRouteById(OnSuccessListener<DtoRoute> listener, int id, String locale);

        void saveRoute(OnLoadRoute<Integer> listener, int id_route);

        void updateRoute(OnProgressListener<String> listener, int id);

        void setCancel(boolean isCancel);
    }
}
