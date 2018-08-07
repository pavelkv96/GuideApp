package com.grsu.guideapp.fragments.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.MessageViewer.Toasts;
import java.util.Arrays;
import java.util.Collections;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.osmdroid.config.Configuration;
import org.osmdroid.mapsforge.MapsForgeTileProvider;
import org.osmdroid.mapsforge.MapsForgeTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.tileprovider.util.StorageUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

public class MapsforgeTileProviderSample extends BaseSampleFragment {

    MapsForgeTileSource fromFiles = null;
    MapsForgeTileProvider forge = null;
    AlertDialog alertDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logs.e(TAG, "onCreate");

        MapsForgeTileSource.createInstance(this.getActivity().getApplication());
    }

    @Override
    public void addOverlays() {
        super.addOverlays();

        File[] maps = findMapFiles();
        if (maps == null) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("No Mapsforge files found");
            alertDialogBuilder.setMessage(
                    "In order to render map tiles, you'll need to either create or obtain mapsforge .map files. See https://github.com/mapsforge/mapsforge for more info. Store them in "
                            + Configuration.getInstance().getOsmdroidBasePath().getAbsolutePath());
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            Context context = getContext();
            Toasts.makeL(context, "Loaded " + maps.length + " map files");
            Logs.e("TAG", "Loaded " + Arrays.toString(maps));

            XmlRenderTheme theme = null;
            try {
                if (context != null) {
                    theme = new AssetsRenderTheme(context.getApplicationContext(), "renderthemes/",
                            "rendertheme-v4.xml");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            fromFiles = MapsForgeTileSource.createFromFiles(maps, theme, "rendertheme-v4");
            forge = new MapsForgeTileProvider(new SimpleRegisterReceiver(context), fromFiles, null);

            mMapView.setTileProvider(forge);

            mMapView.getController().setZoom(13.0);
            mMapView.setMaxZoomLevel(20.0);
            mMapView.setMinZoomLevel(13.0);
            mMapView.setExpectedCenter(new GeoPoint(53.7597, 23.9845));
            mMapView.setScrollableAreaLimitDouble(
                    new BoundingBox(53.9229, 23.8790, 53.7850, 23.5187));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.hide();
            alertDialog.dismiss();
            alertDialog = null;
        }
        if (fromFiles != null) {
            fromFiles.dispose();
        }
        if (forge != null) {
            forge.detach();
        }
        AndroidGraphicFactory.clearResourceMemoryCache();
    }

    protected static File[] findMapFiles() {
        File f = new File(Environment.getExternalStorageDirectory() + "/osmdroid/" + "KA.map");
        if (f.exists()) {
            return new File[]{f};
        }
        return null;
    }

}