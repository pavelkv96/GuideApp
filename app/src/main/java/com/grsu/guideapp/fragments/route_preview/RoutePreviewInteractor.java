package com.grsu.guideapp.fragments.route_preview;

import android.content.Context;
import android.util.Log;
import com.grsu.guideapp.App;
import com.grsu.guideapp.BuildConfig;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnLoadRoute;
import com.grsu.guideapp.database.Table;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.map_preview_v1.MapPreviewInteractor;
import com.grsu.guideapp.models.Route1;
import com.grsu.guideapp.network.model.Datum;
import java.util.List;
import retrofit2.Response;

public class RoutePreviewInteractor extends MapPreviewInteractor implements RoutePreviewContract.TestInteractor {

    private Test test;

    public RoutePreviewInteractor(Test pDbHelper, Context context) {
        super(pDbHelper);
        test = new Test(context);
    }

    private static final String TAG = RoutePreviewInteractor.class.getSimpleName();
    private boolean flag = false;
    private int progress;
    private int max_progress = 100;

    @Override
    public void isDownLoad(final OnFinishedListener<Route1> listener, final int id_route,
            final String locale) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(helper.getRoute(id_route, locale));
            }
        });
    }

    @Override
    public void loadRoute(final OnLoadRoute<String> listener, final int id_route) {
        progress = 0;
        flag = true;

        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Integer> poiFromBD = helper.getListPoiFromBD(id_route);

                    for (Integer id : poiFromBD) {
                        Response<Datum> datum = App.getThread().networkIO()
                                .getPoi(id, BuildConfig.ApiKey).execute();
                        if (datum.isSuccessful()) {
                            test.insertPoiAndTypes(datum.body());
                        } else {
                            Log.e(TAG, "run: " + datum.errorBody());
                        }

                    }

                    /*while (flag || progress <= max_progress) {
                        if (progress < max_progress) {
                            progress += 1;
                            if (progress % 5 == 0) {
                                Logs.e("TAG", "run: " + progress);
                            }
                            Thread.sleep(50);
                        } else {
                            listener.onSuccess("onSuccess");
                            //helper.setDownload(id_route);
                            return;
                            //throw new NullPointerException("Error load route");
                        }
                        if (!flag) {
                            Thread.sleep(2000);
                            listener.onCancelLoad();
                            return;
                        }
                    }*/
                    helper.setDownload(id_route, Table.DOWNLOAD);
                    listener.onSuccess("onSuccess");
                } catch (Exception e) {
                    listener.onFailure(e);
                    Log.e(TAG, "onFailure: ", e);
                }
            }
        });
    }

    @Override
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }


}
