package com.grsu.guideapp.fragments.test;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.grsu.guideapp.database.DatabaseHelper;
import java.util.ArrayList;
import org.osmdroid.util.GeoPoint;

public class TestInteractor implements TestContract.TestInteractor {

    private DatabaseHelper helper;

    public TestInteractor(@NonNull DatabaseHelper pDbHelper) {
        helper = pDbHelper;
    }

    @Override
    public void getRouteById(final OnFinishedListener listener, final Integer routeId) {
        new Handler().post/*Delayed*/(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getRouteById(routeId));
            }
        }/*, 3000*/);
    }

    void d(){
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(53.697250000000004,23.83509,0.0));//0
        geoPoints.add(new GeoPoint(53.698060000000005,23.83508,0.0));//1
        geoPoints.add(new GeoPoint(53.698060000000005,23.83508,0.0));//2
        geoPoints.add(new GeoPoint(53.698240000000006,23.835610000000003,0.0));//3
        geoPoints.add(new GeoPoint(53.698240000000006,23.835610000000003,0.0));//4
        geoPoints.add(new GeoPoint(53.69821,23.83564,0.0));//5
        geoPoints.add(new GeoPoint(53.69798,23.835880000000003,0.0));//6
        geoPoints.add(new GeoPoint(53.69778,23.836060000000003,0.0));//7
        geoPoints.add(new GeoPoint(53.697540000000004,23.836240000000004,0.0));//8
        geoPoints.add(new GeoPoint(53.697340000000004,23.836350000000003,0.0));//9
        geoPoints.add(new GeoPoint(53.69715000000001,23.836430000000004,0.0));//10
        geoPoints.add(new GeoPoint(53.696650000000005,23.83658,0.0));//11
        geoPoints.add(new GeoPoint(53.696630000000006,23.83658,0.0));//12
        geoPoints.add(new GeoPoint(53.696310000000004,23.83667,0.0));//13
        geoPoints.add(new GeoPoint(53.69596000000001,23.83676,0.0));//14
        geoPoints.add(new GeoPoint(53.695690000000006,23.836910000000003,0.0));//15
        geoPoints.add(new GeoPoint(53.695600000000006,23.83697,0.0));//16
        geoPoints.add(new GeoPoint(53.695570000000004,23.836990000000004,0.0));//17
        geoPoints.add(new GeoPoint(53.69548,23.837090000000003,0.0));//18
        geoPoints.add(new GeoPoint(53.69541,23.83716,0.0));//19
        geoPoints.add(new GeoPoint(53.695330000000006,23.83727,0.0));//20
        geoPoints.add(new GeoPoint(53.69525,23.83743,0.0));//21
        geoPoints.add(new GeoPoint(53.695150000000005,23.83762,0.0));//22
        geoPoints.add(new GeoPoint(53.695080000000004,23.83773,0.0));//23
    }
}
