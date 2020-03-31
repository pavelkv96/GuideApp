package com.grsu.guideapp.map

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.grsu.guideapp.App
import com.grsu.guideapp.database.cache.dao.CacheDao
import com.grsu.guideapp.database.cache.entities.CacheTile
import com.grsu.guideapp.utils.MapUtils
import com.grsu.guideapp.utils.extensions.toByteArray
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.graphics.AndroidTileBitmap
import org.mapsforge.map.datastore.MultiMapDataStore
import org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy
import org.mapsforge.map.layer.cache.InMemoryTileCache
import org.mapsforge.map.layer.labels.TileBasedLabelStore
import org.mapsforge.map.layer.renderer.DatabaseRenderer
import org.mapsforge.map.layer.renderer.RendererJob
import org.mapsforge.map.model.DisplayModel
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture
import timber.log.Timber
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService

class TileAdapter(file: File, pProvider: Provider = Provider.MAPSFORGE, dao: CacheDao) {

    private val model = DisplayModel()
    private val scale = DisplayModel.getDefaultUserScaleFactor()
    private var theme: RenderThemeFuture? = null
    private var renderer: DatabaseRenderer? = null
    private var mProvider: Provider

    private var mapDatabase: MultiMapDataStore? = null
    private var cacheDao: CacheDao
    private var executors: ExecutorService? = null

    init {
        executors = App.getInstance().getExecutor() as ExecutorService
        mProvider = pProvider

        if (file.exists()) {
            mapDatabase = MultiMapDataStore(DataPolicy.RETURN_ALL)
            mapDatabase!!.addMapDataStore(MapFile(file), false, false)
        }

        val instance = AndroidGraphicFactory.INSTANCE
        val cache = InMemoryTileCache(2)
        val labelStore = TileBasedLabelStore(cache.capacityFirstLevel)
        renderer = DatabaseRenderer(mapDatabase, instance, cache, labelStore, true, true, null)

        theme = RenderThemeFuture(instance, mProvider, model)
        executors!!.execute(theme!!)

        cacheDao = dao
    }

    fun loadTile(x: Int, y: Int, zoom: Int): Tile {
        val localProvider = mProvider
        val localTheme = theme

        val tileIndex = MapUtils.getTileIndex(x, y, zoom)

        var bytes = cacheDao.getTile(MapUtils.getIndex(tileIndex), localProvider)
        if (bytes != null && localProvider == mProvider) {
            Timber.d("load next tile ${MapUtils.getIndex(tileIndex)} with provider $localProvider")
            return Tile(256, 256, bytes)
        }

        val image = synchronized(this) { renderTile(localTheme, x, y, zoom) }
        if (image != null && localProvider == mProvider) {
            try {
                bytes = image.toByteArray()
                cacheDao.saveTile(CacheTile(MapUtils.getIndex(tileIndex), localProvider, bytes, Date()))
                Timber.d("Saved tile ${MapUtils.getIndex(tileIndex)} with provider $localProvider")
                return Tile(256, 256, bytes)
            } catch (ex: Exception) {
                Timber.e(ex, "Error storing tile cache with provider $localProvider")
            }
        }

        return TileProvider.NO_TILE
    }

    private fun renderTile(localTheme: RenderThemeFuture?, x: Int, y: Int, zoom: Int): Bitmap? {
        if (mapDatabase == null) {
            return null
        }
        try {
            val tile = MapUtils.getTile(x, y, zoom, 512)
            val job = RendererJob(tile, mapDatabase, localTheme, model, scale, false, false)
            val bmp = renderer!!.executeJob(job) as? AndroidTileBitmap
            if (bmp != null) {
                return AndroidGraphicFactory.getBitmap(bmp)
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Mapsforge tile generation failed with provider $mProvider")
        }
        return null
    }

    fun getBoundingBox(): LatLngBounds? = mapDatabase?.run {
        val box = boundingBox()
        val nw = LatLng(box.minLatitude, box.minLongitude)
        val se = LatLng(box.maxLatitude, box.maxLongitude)
        LatLngBounds(nw, se)
    }

    fun setProvider(provider: Provider = Provider.MAPSFORGE) {
        mProvider = provider
        theme = RenderThemeFuture(AndroidGraphicFactory.INSTANCE, mProvider, model)
        executors!!.execute(theme!!)
    }

    fun dispose() {
        executors = null
        theme?.decrementRefCount()
        theme = null
        renderer = null
        mapDatabase?.close()
        mapDatabase = null
    }
}
