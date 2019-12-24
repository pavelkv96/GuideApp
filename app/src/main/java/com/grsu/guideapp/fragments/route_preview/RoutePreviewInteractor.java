package com.grsu.guideapp.fragments.route_preview;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.grsu.guideapp.App;
import com.grsu.guideapp.base.listeners.OnLoadRoute;
import com.grsu.guideapp.base.listeners.OnProgressListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.database.Table;
import com.grsu.guideapp.database.Test;
import com.grsu.guideapp.fragments.route_preview.RoutePreviewContract.RouteInteractor;
import com.grsu.guideapp.models.DtoRoute;
import com.grsu.guideapp.network.model.Datum;
import java.util.List;
import retrofit2.Response;

class RoutePreviewInteractor implements RouteInteractor {

    private static final String TAG = RoutePreviewInteractor.class.getSimpleName();

    private Test mHelper;
    private boolean isCancel = true;

    RoutePreviewInteractor(Test pHelper) {
        mHelper = pHelper;
    }

    @Override
    public void getRouteById(final OnSuccessListener<DtoRoute> listener, final int id,
            final String locale) {
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(mHelper.getRoute(id, locale));
                    //throw new NullPointerException("No such route with id " + id);
                } catch (Exception e) {
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void saveRoute(final OnLoadRoute<Integer> listener, final int id_route) {
        /*isCancel = false;
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    float currentProgress = 0;
                    float partProgress = (float) 100 / (20 + 1);

                    listener.onProgress((int) currentProgress);
                    Thread.sleep(1500);
                    listener.onProgress((int) (currentProgress += partProgress));
                    for (int i = 0; i < 20; i++) {
                        if (!isCancel) {
                            Thread.sleep(500);
                            listener.onProgress((int) (currentProgress += partProgress));
                        } else {
                            listener.onCancelLoad();
                        }
                    }
                } catch (InterruptedException e) {
                    listener.onFailure(e);
                }
                if (!isCancel) {
                    listener.onSuccess(android.R.string.ok);
                }
            }
        });*/
        isCancel = false;
        App.getThread().diskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Integer> poiFromBD = mHelper.getListPoiFromBD(id_route);
                    float currentProgress = 0;
                    float partProgress = (float) 100 / (poiFromBD.size() + 1);

                    listener.onProgress((int) (currentProgress += partProgress));
                    SQLiteDatabase database = mHelper.getWritableDatabase();
                    database.beginTransaction();
                    try {
                        for (Integer id : poiFromBD) {
                            Response<Datum> datum = App.getThread().networkIO()
                                    .getPoi(id).execute();
                            if (!isCancel) {
                                mHelper.insertPoiAndTypesTransaction(database, datum.body());
                                listener.onProgress((int) (currentProgress += partProgress));
                            } else {
                                Log.e(TAG, "run: " + datum.errorBody());
                                listener.onCancelLoad();
                                break;
                            }
                        }
                        if (!isCancel) {
                            database.setTransactionSuccessful();
                        }
                    } finally {
                        database.endTransaction();
                    }
                    if (!isCancel) {
                        mHelper.setDownload(id_route, Table.DOWNLOAD);
                        listener.onSuccess(android.R.string.ok);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void updateRoute(final OnProgressListener<String> listener, int id) {
        mHelper.updateFullRouteById(listener, id);
    }

    @Override
    public void setCancel(boolean cancel) {
        this.isCancel = cancel;
    }
}
