package com.grsu.guideapp.activities.details;

import android.graphics.Bitmap;
import com.grsu.guideapp.R;
import com.grsu.guideapp.activities.details.DetailsContract.DetailsView;
import com.grsu.guideapp.base.BasePresenterImpl;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.InfoAboutPoi;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import java.io.File;

public class DetailsPresenter extends BasePresenterImpl<DetailsView> implements
        DetailsContract.DetailsPresenter {

    private static final String TAG = DetailsPresenter.class.getSimpleName();

    private DetailsView detailsView;
    private DetailsInteractor detailsInteractor;

    public DetailsPresenter(DetailsView detailsView, DetailsInteractor detailsInteractor) {
        this.detailsView = detailsView;
        this.detailsInteractor = detailsInteractor;
    }

    @Override
    public void getById(String idPoint) {
        detailsInteractor.getInfoById(new OnFinishedListener<InfoAboutPoi>() {
            @Override
            public void onFinished(InfoAboutPoi poi) {
                Logs.e(TAG, poi + "");
                detailsView.setContent(poi);
                getImageByName("drb");
            }
        }, idPoint);
    }

    @Override
    public void getImageByName(String imageName) {
        File content = new File(Settings.PHOTO_CONTENT, imageName + ".png");
        detailsInteractor.getImageFromStorage(new OnSuccessListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                detailsView.showImage(bitmap);
            }

            @Override
            public void onFail(Throwable throwable) {
                Logs.e(TAG, throwable.getMessage(), throwable);
                detailsView.showImage(R.mipmap.ic_launcher_round);
            }
        }, content);
    }

}
