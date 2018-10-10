package com.grsu.guideapp.mf;

import static com.grsu.guideapp.utils.MapUtils.getIndex;
import static com.grsu.guideapp.utils.MapUtils.getX;
import static com.grsu.guideapp.utils.MapUtils.getY;
import static com.grsu.guideapp.utils.MapUtils.getZoom;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import com.grsu.guideapp.databases.CacheDBHelper;
import com.grsu.guideapp.utils.MessageViewer.Logs;
import com.grsu.guideapp.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
        mapDatabase = new MultiMapDataStore(DataPolicy.RETURN_ALL);
        mapDatabase.addMapDataStore(new MapFile(file), false, false);

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
        new Thread(theme).start();
    }

    @Nullable
    private static synchronized Drawable renderTile(final long pMapTileIndex) {

        Tile tile = new Tile(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex), 256);

        if (mapDatabase == null) {
            return null;
        }
        try {
            RendererJob mapGeneratorJob = new RendererJob(tile, mapDatabase, theme, model, scale,
                    false, false);
            AndroidTileBitmap bmp = (AndroidTileBitmap) renderer.executeJob(mapGeneratorJob);
            if (bmp != null) {
                return new BitmapDrawable(AndroidGraphicFactory.getBitmap(bmp));
            }
        } catch (Exception ex) {
            Logs.e(TAG, "Mapsforge tile generation failed", ex);
        }
        /*Bitmap bitmap = Bitmap.createBitmap(TILE_SIZE_PIXELS, TILE_SIZE_PIXELS, RGB_565);
        bitmap.eraseColor(Color.GRAY);
        return new BitmapDrawable(bitmap);*/
        return null;
    }

    public static byte[] loadTile(final long pMapTileIndex) {
        byte[] bytes = CacheDBHelper.getTile(getIndex(pMapTileIndex), mProvider);
        if (bytes != null) {
            Logs.e(TAG, "load next tile " + getIndex(pMapTileIndex));
            return bytes;
        }

        Drawable image = renderTile(pMapTileIndex);

        if (image instanceof BitmapDrawable) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ((BitmapDrawable) image).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            StreamUtils.closeStream(stream);

            //Set image in database
            ByteArrayInputStream bais = null;
            try {
                bais = new ByteArrayInputStream(bitmapdata);
                CacheDBHelper.saveTile(pMapTileIndex, mProvider, bais, null);
                bytes = CacheDBHelper.getTile(getIndex(pMapTileIndex), mProvider);
                Logs.e(TAG, "Saved tile " + getIndex(pMapTileIndex));
            } catch (Exception ex) {
                Logs.e(TAG, "forge error storing tile cache", ex);
            } finally {
                StreamUtils.closeStream(bais);
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
}
