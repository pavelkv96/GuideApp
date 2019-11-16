package com.grsu.guideapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.project_settings.Settings;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StorageUtils;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.concurrent.Executors;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidTileBitmap;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.labels.TileBasedLabelStore;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

public class TileAdapter {

    private static final String TAG = TileAdapter.class.getSimpleName();
    private static final DisplayModel model = new DisplayModel();
    private static final float scale = DisplayModel.getDefaultUserScaleFactor();
    private static RenderThemeFuture theme;
    private static DatabaseRenderer renderer;
    private static String mProvider;
    private static int imageSize;

    private static MultiMapDataStore mapDatabase;

    public static void createInstance(File file, Context context, String provider) {
        XmlRenderTheme renderTheme = null;
        try {
//            renderTheme = new AssetsRenderTheme(context, Settings.THEME_FOLDR, Settings.THEME_FILE);
        } catch (Exception ignore) {
        } finally {
            if (renderTheme == null) {
                renderTheme = InternalRenderTheme.OSMARENDER;
            }
        }
        mProvider = provider;

        if (mapDatabase == null) {
            mapDatabase = new MultiMapDataStore(DataPolicy.RETURN_ALL);
            mapDatabase.addMapDataStore(new MapFile(file), false, false);
        }

        AndroidGraphicFactory instance = AndroidGraphicFactory.INSTANCE;
        if (instance == null) {
            throw new RuntimeException("Must call AndroidGraphicFactory.createInstance(app)");
        }

        imageSize = VERSION.SDK_INT < VERSION_CODES.M ? 256 : 512;
        InMemoryTileCache cache = new InMemoryTileCache(2);
        TileBasedLabelStore labelStore = new TileBasedLabelStore(cache.getCapacityFirstLevel());
        renderer = new DatabaseRenderer(mapDatabase, instance, cache, labelStore, true, true, null);

        theme = new RenderThemeFuture(instance, renderTheme, model);
        Executors.newFixedThreadPool(3).execute(theme);
    }

    @Nullable
    private static synchronized Bitmap renderTile(final long pMapTileIndex) {
        if (mapDatabase == null) {
            return null;
        }

        try {
            Tile tile = MapUtils.getTile(pMapTileIndex, 512);
            RendererJob job = new RendererJob(tile, mapDatabase, theme, model, scale, false, false);
            AndroidTileBitmap bmp = (AndroidTileBitmap) renderer.executeJob(job);
            if (bmp != null) {
                return AndroidGraphicFactory.getBitmap(bmp);
            }
        } catch (Exception ex) {
            Logs.e(TAG, "Mapsforge tile generation failed", ex);
        }

        return null;
    }

    public static byte[] loadTile(final long pMapTileIndex) {
        byte[] bytes = CacheDBHelper.getTile(MapUtils.getIndex(pMapTileIndex), mProvider);
        if (bytes != null) {
            Logs.e(TAG, "load next tile " + MapUtils.getIndex(pMapTileIndex));
            return bytes;
        }

        Bitmap image = renderTile(pMapTileIndex);

        if (image != null) {
            if (imageSize != 512) {
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            }
            ByteArrayInputStream bais = null;
            try {
                byte[] bitmapdata = StorageUtils.toByteArray(image);
                //Set image in database
                bais = new ByteArrayInputStream(bitmapdata);
                CacheDBHelper.saveTile(pMapTileIndex, mProvider, bais, null);
                Logs.e(TAG, "Saved tile " + MapUtils.getIndex(pMapTileIndex));
                return bitmapdata;
            } catch (Exception ex) {
                Logs.e(TAG, "forge error storing tile cache", ex);
            } finally {
                StreamUtils.closeStream(bais);
            }
        }
        return null;
    }

    public static void dispose() {
        CacheDBHelper.disconnectDB();
        if (theme != null) {
            theme.decrementRefCount();
        }
        theme = null;
        renderer = null;
        if (mapDatabase != null) {
            mapDatabase.close();
        }
        mapDatabase = null;
    }

    public static LatLngBounds getBoundingBox(File file) {
        if (mapDatabase == null) {
            mapDatabase = new MultiMapDataStore(DataPolicy.RETURN_ALL);
            mapDatabase.addMapDataStore(new MapFile(file), false, false);
        }

        BoundingBox box = mapDatabase.boundingBox();
        LatLng nw = new LatLng(box.minLatitude, box.minLongitude);
        LatLng se = new LatLng(box.maxLatitude, box.maxLongitude);
        return new LatLngBounds(nw, se);
    }
}
