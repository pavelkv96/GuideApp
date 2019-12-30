package com.grsu.guideapp.adapters

import android.graphics.Bitmap
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.room.Room
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.grsu.guideapp.App
import com.grsu.guideapp.database.cache.CacheDataBase
import com.grsu.guideapp.database.cache.dao.CacheDao
import com.grsu.guideapp.database.cache.entities.CacheTile
import com.grsu.guideapp.project_settings.NewSettings
import com.grsu.guideapp.utils.MapUtils
import com.grsu.guideapp.utils.MessageViewer.Logs
import com.grsu.guideapp.utils.Provider
import com.grsu.guideapp.utils.extensions.toByteArray
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.graphics.AndroidTileBitmap
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme
import org.mapsforge.map.datastore.MultiMapDataStore
import org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy
import org.mapsforge.map.layer.cache.InMemoryTileCache
import org.mapsforge.map.layer.labels.TileBasedLabelStore
import org.mapsforge.map.layer.renderer.DatabaseRenderer
import org.mapsforge.map.layer.renderer.RendererJob
import org.mapsforge.map.model.DisplayModel
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import org.mapsforge.map.rendertheme.XmlRenderTheme
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture
import java.io.File
import java.util.*
import java.util.concurrent.Executors

object TileAdapter {

    private val TAG = TileAdapter::class.java.simpleName

    private val model = DisplayModel()
    private val scale = DisplayModel.getDefaultUserScaleFactor()
    private var theme: RenderThemeFuture? = null
    private var renderer: DatabaseRenderer? = null
    private lateinit var mProvider: Provider
    private var imageSize = if (VERSION.SDK_INT < VERSION_CODES.M) 256 else 512

    private var mapDatabase: MultiMapDataStore? = null
    private var dataBase: CacheDataBase? = null
    private lateinit var cacheDao: CacheDao

    fun createConnection(file: File, pProvider: Provider = Provider.FORGE) {
        var renderTheme: XmlRenderTheme? = null
        try {
            renderTheme = AssetsRenderTheme(App.getInstance(), NewSettings.MAPSFORGE_FOLDER, NewSettings.THEME_FILE)
        } catch (ignore: Exception) {
        } finally {
            if (renderTheme == null) {
                renderTheme = InternalRenderTheme.DEFAULT
            }
        }

        mProvider = pProvider

        if (mapDatabase == null && file.exists()) {
            mapDatabase = MultiMapDataStore(DataPolicy.RETURN_ALL)
            mapDatabase!!.addMapDataStore(MapFile(file), false, false)
        }

        val instance = AndroidGraphicFactory.INSTANCE
        val cache = InMemoryTileCache(2)
        val labelStore = TileBasedLabelStore(cache.capacityFirstLevel)
        renderer = DatabaseRenderer(mapDatabase, instance, cache, labelStore, true, true, null)

        theme = RenderThemeFuture(instance, renderTheme, model)
        Executors.newFixedThreadPool(3).execute(theme)

        dataBase = Room.databaseBuilder(App.getInstance(), CacheDataBase::class.java, "map cache.db").build()
        dataBase?.apply { cacheDao = cache() }
    }

    fun loadTile(x: Int, y: Int, zoom: Int): Tile {
        val tileIndex = MapUtils.getTileIndex(x, y, zoom)

        var bytes = cacheDao.getTile(MapUtils.getIndex(tileIndex), mProvider)
        if (bytes != null) {
            Logs.e(TAG, "load next tile ${MapUtils.getIndex(tileIndex)}")
            return Tile(256, 256, bytes)
        }

        val image = renderTile(x, y, zoom)
        if (image != null) {
            try {
                bytes = image.toByteArray()
                cacheDao.saveTile(CacheTile(MapUtils.getIndex(tileIndex), mProvider, bytes, Date()))
                Logs.e(TAG, "Saved tile ${MapUtils.getIndex(tileIndex)}")
                return Tile(256, 256, bytes)
            } catch (ex: Exception) {
                Logs.e(TAG, "Error storing tile cache", ex)
            }
        }

        return TileProvider.NO_TILE
    }

    fun dispose() {
        dataBase?.close()
        theme?.decrementRefCount()
        theme = null
        renderer = null
        mapDatabase?.close()
        mapDatabase = null
    }

    fun getBoundingBox(file: File): LatLngBounds? {
        if (mapDatabase == null && file.exists()) {
            mapDatabase = MultiMapDataStore(DataPolicy.RETURN_ALL)
            mapDatabase?.addMapDataStore(MapFile(file), false, false)
        }
        return mapDatabase?.let {
            val box = it.boundingBox()
            val nw = LatLng(box.minLatitude, box.minLongitude)
            val se = LatLng(box.maxLatitude, box.maxLongitude)
            LatLngBounds(nw, se)
        }
    }

    @Synchronized
    private fun renderTile(x: Int, y: Int, zoom: Int): Bitmap? {
        if (mapDatabase == null) {
            return null
        }
        try {
            val tile = MapUtils.getTile(x, y, zoom, imageSize)
            val job = RendererJob(tile, mapDatabase, theme, model, scale, false, false)
            val bmp = renderer!!.executeJob(job) as? AndroidTileBitmap
            if (bmp != null) {
                return AndroidGraphicFactory.getBitmap(bmp)
            }
        } catch (ex: Exception) {
            Logs.e(TAG, "Mapsforge tile generation failed", ex)
        }
        return null
    }

    @Suppress("unused")
    private fun setProvider(provider: Provider = Provider.FORGE) {
        mProvider = provider
    }
}