package com.grsu.guideapp.activities.route;

import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.models.Line;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public interface RouteContract {

    interface RouteView extends BaseView {

        void mapViewSettings();

        void setPolyLine(List<GeoPoint> geoPointList);

        void setPoints(GeoPoint geoPoint);
    }


    interface RoutePresenter extends BasePresenter<RouteView> {

        void getID(Integer id);
    }


    interface RouteInteractor {

        interface OnFinishedListener {

            void onFinished(List<Line> encodePolylines);
        }

        void getRouteById(OnFinishedListener listener, Integer id);
    }
}
