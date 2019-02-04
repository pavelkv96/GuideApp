package com.grsu.guideapp.mf;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.grsu.guideapp.database.CacheDBHelper;
import com.grsu.guideapp.utils.MapUtils;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Executors;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidTileBitmap;
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

public class MapsForgeTileSource {

    private static final String TAG = MapsForgeTileSource.class.getSimpleName();
    private static final DisplayModel model = new DisplayModel();
    private static final float scale = DisplayModel.getDefaultUserScaleFactor();
    private static RenderThemeFuture theme;
    private static DatabaseRenderer renderer;
    private static String mProvider;

    private static MultiMapDataStore mapDatabase;

    public static void createFromFiles(File file, XmlRenderTheme renderTheme, String provider) {
        mProvider = provider;

        if (mapDatabase == null) {
            mapDatabase = new MultiMapDataStore(DataPolicy.RETURN_ALL);
            mapDatabase.addMapDataStore(new MapFile(file), false, false);
        }

        if (AndroidGraphicFactory.INSTANCE == null) {
            throw new RuntimeException(
                    "Must call MapsForgeTileSource.createInstance(context.getApplication()); once before MapsForgeTileSource.createFromFiles().");
        }

        InMemoryTileCache tileCache = new InMemoryTileCache(2);
        TileBasedLabelStore labelStore = new TileBasedLabelStore(tileCache.getCapacityFirstLevel());
        renderer = new DatabaseRenderer(
                mapDatabase,
                AndroidGraphicFactory.INSTANCE,
                tileCache,
                labelStore,
                true,
                true,
                null);

        if (renderTheme == null) {
            renderTheme = InternalRenderTheme.OSMARENDER;
        }
        theme = new RenderThemeFuture(AndroidGraphicFactory.INSTANCE, renderTheme, model);
        Executors.newFixedThreadPool(3).execute(theme);
    }

    @Nullable
    private static synchronized Bitmap renderTile(final long pMapTileIndex) {

        Tile tile = MapUtils.getTile(pMapTileIndex, 600);

        if (mapDatabase == null) {
            return null;
        }
        try {
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
            ByteArrayOutputStream stream = null;
            ByteArrayInputStream bais = null;
            try {
                stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();

                //Set image in database
                bais = new ByteArrayInputStream(bitmapdata);
                CacheDBHelper.saveFile(pMapTileIndex, mProvider, bais, null);
                bytes = CacheDBHelper.getTile(MapUtils.getIndex(pMapTileIndex), mProvider);
                Logs.e(TAG, "Saved tile " + MapUtils.getIndex(pMapTileIndex));
            } catch (Exception ex) {
                Logs.e(TAG, "forge error storing tile cache", ex);
            } finally {
                StreamUtils.closeStream(bais);
                StreamUtils.closeStream(stream);
            }
        }
        return bytes;
    }

    public static void dispose() {
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
