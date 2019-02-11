package com.grsu.guideapp.fragments.details;

import android.graphics.Bitmap;
import com.grsu.guideapp.base.BasePresenter;
import com.grsu.guideapp.base.BaseView;
import com.grsu.guideapp.base.listeners.OnFinishedListener;
import com.grsu.guideapp.base.listeners.OnSuccessListener;
import com.grsu.guideapp.models.InfoAboutPoi;
import java.io.File;

public interface DetailsContract extends OnSuccessListener, OnFinishedListener {

    interface DetailsView extends BaseView {

        void showImage(Bitmap image);

        void showImage(int resource);

        void setContent(InfoAboutPoi content);

        void returnedIntent(File content);

        void hideButton();

        void showButton();
    }

    interface DetailsPresenter extends BasePresenter<DetailsView> {

        void getById(String idPoint);

        void getImageByName(String imageName);

        void getAudio(String name);
    }

    interface DetailsInteractor {

        void getInfoById(OnFinishedListener<InfoAboutPoi> listener, String id);

        void getImageFromStorage(OnSuccessListener<Bitmap> listener, String name);

        void getAudioFromStorage(OnSuccessListener<File> listener, String name);
    }
}
