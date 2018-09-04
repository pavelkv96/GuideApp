package com.grsu.guideapp.mf;

import static android.graphics.Bitmap.Config.RGB_565;
import static com.grsu.guideapp.utils.MapUtils.getIndex;
import static com.grsu.guideapp.utils.MapUtils.getX;
import static com.grsu.guideapp.utils.MapUtils.getY;
import static com.grsu.guideapp.utils.MapUtils.getZoom;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import com.grsu.guideapp.database.CacheDBHelper;
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
    private final static int TILE_SIZE_PIXELS = 256;
    private static final DisplayModel model = new DisplayModel();
    private static final float scale = DisplayModel.getDefaultUserScaleFactor();
    private static RenderThemeFuture theme;
    private static DatabaseRenderer renderer;
    private static String mProvider;

    private static MultiMapDataStore mapDatabase;

    public MapsForgeTileSource(File[] file, XmlRenderTheme xmlRenderTheme, String provider) {
        mProvider = provider;
        mapDatabase = new MultiMapDataStore(DataPolicy.RETURN_ALL);
        for (File aFile : file) {
            mapDatabase.addMapDataStore(new MapFile(aFile), false, false);
        }

        if (AndroidGraphicFactory.INSTANCE == null) {
            throw new RuntimeException(
                    "Must call MapsForgeTileSource.createInstance(context.getApplication()); once before MapsForgeTileSource.createFromFiles().");
        }

        // mapsforge0.8.0
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

        if (xmlRenderTheme == null) {
            xmlRenderTheme = InternalRenderTheme.OSMARENDER;
        }
        //we the passed in theme is different that the existing one, or the theme is currently null, create it
        theme = new RenderThemeFuture(AndroidGraphicFactory.INSTANCE, xmlRenderTheme, model);
        //super important!! without the following line, all rendering activities will block until the theme is created.
        new Thread(theme).start();
    }

    public static void createFromFiles(File[] file, XmlRenderTheme theme, String provider) {
        new MapsForgeTileSource(file, theme, provider);
    }

    @Nullable
    public static synchronized Drawable renderTile(final long pMapTileIndex) {

        Tile tile = new Tile(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex), 256);

        if (mapDatabase == null) {
            return null;
        }
        try {
            //Draw the tile
            RendererJob mapGeneratorJob = new RendererJob(tile, mapDatabase, theme, model, scale,
                    false, false);
            AndroidTileBitmap bmp = (AndroidTileBitmap) renderer.executeJob(mapGeneratorJob);
            if (bmp != null) {
                return new BitmapDrawable(AndroidGraphicFactory.getBitmap(bmp));
            }
        } catch (Exception ex) {
            Logs.e(TAG, "Mapsforge tile generation failed", ex);
        }
        //Make the bad tile easy to spot
        Bitmap bitmap = Bitmap.createBitmap(TILE_SIZE_PIXELS, TILE_SIZE_PIXELS, RGB_565);
        bitmap.eraseColor(Color.GRAY);
        return new BitmapDrawable(bitmap);
    }

    public static byte[] loadTile(final long pMapTileIndex) {
        byte[] bytes = CacheDBHelper.getTile(getIndex(pMapTileIndex), mProvider);
        if (bytes != null) {
            Logs.e(TAG, "load next tile " + getIndex(pMapTileIndex));
            return bytes;
        }

        Drawable image = renderTile(pMapTileIndex);

        if (image != null && image instanceof BitmapDrawable) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ((BitmapDrawable) image).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            StreamUtils.closeStream(stream);

            //TODO Set image in database

            ByteArrayInputStream bais = null;
            try {
                bais = new ByteArrayInputStream(bitmapdata);
                CacheDBHelper.saveFile(pMapTileIndex, mProvider, bais, null);
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
