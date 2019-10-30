package com.grsu.guideapp.fragments.open_route;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.models.Line;
import com.grsu.guideapp.models.Poi;
import java.util.List;

public interface RoutePreviewContract {

    interface RouteViews extends BaseView {

        void setBounds(DtoRoute route);

        void setLines(List<Line> lines);

        void setPoi(List<Poi> poi);
    }

    interface RoutePresenter extends BasePresenter<RouteViews> {

        void getRouteById(int id);

        void getLines(int id);

        void getPoi(int id);
    }

    interface RouteInteractor {

        void getRouteById(OnSuccessListener<DtoRoute> listener, int id);

        void getLines(OnSuccessListener<List<Line>> listener, int id);

        void getPoi(OnSuccessListener<List<Poi>> listener, int id);
    }
}
