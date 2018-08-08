package com.grsu.guideapp.views.infowindows;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.grsu.guideapp.R;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomBasicInfoWindow extends InfoWindow {

    private static final String TAG = CustomBasicInfoWindow.class.getSimpleName();

    private static final int UNDEFINED_RES_ID = 0;

    private static int mTitleId = UNDEFINED_RES_ID,
            mDescriptionId = UNDEFINED_RES_ID,
            mSubDescriptionId = UNDEFINED_RES_ID;
    static int mImageId = UNDEFINED_RES_ID; //resource ids

    private static void setResIds(Context context) {
        String packageName = context.getPackageName(); //get application package name
        mTitleId = context.getResources().getIdentifier("id/bubble_title", null, packageName);
        mDescriptionId = context.getResources()
                .getIdentifier("id/bubble_description", null, packageName);
        mSubDescriptionId = context.getResources()
                .getIdentifier("id/bubble_subdescription", null, packageName);
        mImageId = context.getResources().getIdentifier("id/bubble_image", null, packageName);
        if (mTitleId == UNDEFINED_RES_ID || mDescriptionId == UNDEFINED_RES_ID
                || mSubDescriptionId == UNDEFINED_RES_ID || mImageId == UNDEFINED_RES_ID) {
            Logs.e(IMapView.LOGTAG, "BasicInfoWindow: unable to get res ids in " + packageName);
        }
    }

    public CustomBasicInfoWindow(MapView mapView) {
        this(R.layout.bubble, mapView);
    }

    public CustomBasicInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);

        if (mTitleId == UNDEFINED_RES_ID) {
            setResIds(mapView.getContext());
        }

        //default behavior: close it when clicking on the bubble:
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        view.performClick();
                        //close();
                    }
                    break;
                }

                return true;
            }
        });
    }

    @Override
    public void onOpen(Object item) {
        OverlayWithIW overlay = (OverlayWithIW) item;
        String title = overlay.getTitle();
        if (title == null) {
            title = "";
        }
        if (mView == null) {
            Logs.e(TAG, "Error trapped, BasicInfoWindow.open, mView is null!");
            return;
        }
        TextView temp = mView.findViewById(mTitleId /*R.id.title*/);

        if (temp != null) {
            temp.setText(title);
        }

        String snippet = overlay.getSnippet();
        if (snippet == null) {
            snippet = "";
        }
        Spanned snippetHtml = Html.fromHtml(snippet);
        ((TextView) mView.findViewById(mDescriptionId /*R.id.description*/)).setText(snippetHtml);

        //handle sub-description, hidding or showing the text view:
        TextView subDescText = mView.findViewById(mSubDescriptionId);
        String subDesc = overlay.getSubDescription();
        if (subDesc != null && !("".equals(subDesc))) {
            subDescText.setText(Html.fromHtml(subDesc));
            subDescText.setVisibility(View.VISIBLE);
        } else {
            subDescText.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClose() {
        //by default, do nothing
    }
}
