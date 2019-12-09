package com.grsu.guideapp.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.TileOverlay
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.TileProvider
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.adapters.TileAdapter
import com.grsu.guideapp.project_settings.Settings
import com.grsu.guideapp.utils.CheckPermission
import com.grsu.guideapp.utils.MessageViewer.Logs
import com.grsu.guideapp.utils.Provider
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import java.io.File

class TestActivity : AppCompatActivity(), OnMapReadyCallback, TileProvider, GoogleMap.OnCameraMoveStartedListener {

    private var mMap: GoogleMap? = null
    private var overlay: TileOverlay? = null
    private var borders: LatLngBounds? = null
    private val tag = TestActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findViewById<Button>(R.id.button).setOnClickListener {
            overlay?.clearTileCache()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.apply {
            /*it.*/isMyLocationEnabled = false
            /*it.*/mapType = GoogleMap.MAP_TYPE_NONE
            /*it.*/setMinZoomPreference(Settings.MIN_ZOOM_LEVEL)
            /*it.*/setMaxZoomPreference(Settings.MAX_ZOOM_LEVEL)
            /*it.*/uiSettings?.isMapToolbarEnabled = false
            /*it.*/setOnCameraMoveStartedListener(this@TestActivity)
        }
        val file: File = getDatabasePath(Settings.MAP_FILE)
        borders = TileAdapter.getBoundingBox(file)
        mMap?.setLatLngBoundsForCameraTarget(borders)
        overlay = mMap?.addTileOverlay(TileOverlayOptions().tileProvider(this))
    }

    override fun onStart() {
        super.onStart()
        val file: File = getDatabasePath(Settings.MAP_FILE)
        if (!CheckPermission.checkStoragePermission(this) || !file.exists()) {
            finish()
            return
        }
        AndroidGraphicFactory.createInstance(App.getInstance())
        TileAdapter.createConnection(file, Provider.FORGE)
    }

    override fun onStop() {
        super.onStop()
        overlay?.clearTileCache()
        TileAdapter.dispose()
        AndroidGraphicFactory.clearResourceMemoryCache()
    }

    override fun getTile(x: Int, y: Int, zoom: Int) = TileAdapter.loadTile(x, y, zoom)

    override fun onDestroy() {
        overlay?.remove()
        super.onDestroy()
    }

    override fun onCameraMoveStarted(reason: Int) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
            mMap?.cameraPosition?.let {
                Logs.e(tag, "current zoom level = ${it.zoom}")
            }
        }
    }
}
