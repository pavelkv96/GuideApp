package com.grsu.guideapp.map

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grsu.guideapp.App
import com.grsu.guideapp.NavMainGraphDirections
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.activities.SharedViewModel
import com.grsu.guideapp.data.local.PreferenceManager
import com.grsu.guideapp.project_settings.Settings
import com.grsu.guideapp.project_settings.RequestsCodes
import com.grsu.guideapp.ui.custom.MyLocationLayer
import com.grsu.guideapp.utils.CheckPermission
import com.grsu.guideapp.utils.extensions.hide
import com.grsu.guideapp.utils.extensions.navigate
import com.grsu.guideapp.utils.extensions.popBackStack
import com.grsu.guideapp.utils.extensions.show
import com.grsu.guideapp.ui.custom.scale.MapScaleView
import kotlinx.coroutines.Runnable
import java.io.File

abstract class BaseMapFragment : Fragment(), OnMapReadyCallback, TileProvider, OnMarkerClickListener, OnCameraIdleListener,
    Runnable, View.OnClickListener {

    protected abstract var resMap:Int?

    protected var map: GoogleMap? = null
    protected var myLocation: Pair<MarkerOptions, CircleOptions>? = null
    private var borders: LatLngBounds? = null
    private var overlay: TileOverlay? = null

    private lateinit var scaleView: MapScaleView
    private lateinit var layersButton: FloatingActionButton
    private lateinit var zoomInButton: FloatingActionButton
    private lateinit var zoomOutButton: FloatingActionButton

    private var handler: Handler? = null

    private var previousZoom: Float = 20F

    private lateinit var model: MapFragmentModel// by viewModels()
    private val sharedModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { previousZoom = savedInstanceState.getFloat(key_zoom, 20F) }
        File(requireActivity().filesDir, Settings.MAP_FILE).also {
            val provider = PreferenceManager.getProvider()
            if (!it.exists()) return@also
            val factory = ModelFactory(it, Provider.valueOf(provider))
            model = ViewModelProvider(this, factory)[MapFragmentModel::class.java]
            sharedModel.getChoiceDialogResult().observe(this, Observer { result -> model.setChoiceDialog(result) })
            model.getProvider().observe(this, Observer { overlay?.clearTileCache() })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(resMap ?: R.layout.base_map, container, false)
        view?.also {
            scaleView = it.findViewById(R.id.msv_base_map_scale)
            layersButton = it.findViewById(R.id.fab_base_map_layer)
            zoomInButton = it.findViewById(R.id.fab_base_map_zoom_in)
            zoomOutButton = it.findViewById(R.id.fab_base_map_zoom_out)

            layersButton.setOnClickListener(this)
            zoomInButton.setOnClickListener { map?.animateCamera(CameraUpdateFactory.zoomIn()) }
            zoomOutButton.setOnClickListener { map?.animateCamera(CameraUpdateFactory.zoomOut()) }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.f_only_map)
        (mapFragment as? SupportMapFragment)?.getMapAsync(this)
        return view
    }

    override fun onStart() {
        super.onStart()
        if (!CheckPermission.checkStoragePermission(requireContext())) {
            popBackStack()
            Toast.makeText(
                App.getInstance(),
                R.string.error_snackbar_do_not_have_permission_write_on_the_storage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val position = map?.cameraPosition
        outState.putFloat(key_zoom, position?.zoom ?: previousZoom)
        outState.putFloat(key_tilt, position?.tilt ?: 0F)
        outState.putFloat(key_bearing, position?.bearing ?: 0F)
        outState.putParcelable(key_target, position?.target ?: LatLng(0.0, 0.0))
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        overlay?.clearTileCache()
        handler?.removeCallbacks(this)
        super.onStop()
    }

    override fun onDestroyView() {
        overlay?.clearTileCache()
        overlay?.remove()
        super.onDestroyView()
    }

    override fun onMapReady(_map: GoogleMap) {
        map = _map
        handler = Handler()

        map?.also {
            it.mapType = GoogleMap.MAP_TYPE_NONE
            it.setMinZoomPreference(10F)
            it.setMaxZoomPreference(20F)
            it.uiSettings.isMapToolbarEnabled = false
            it.setOnCameraIdleListener(this)
        }?.also {
            borders = model.getBoxingBounds()
            it.setLatLngBoundsForCameraTarget(borders)
        }?.also {
            val tileOptions = TileOverlayOptions().tileProvider { x, y, zoom -> model.loadTile(x, y, zoom) }
            overlay = it.addTileOverlay(tileOptions)
        }
        myLocation = MyLocationLayer.getMyLocationLayer()
    }

    override fun getTile(x: Int, y: Int, zoom: Int): Tile = TileProvider.NO_TILE

    override fun onMarkerClick(marker: Marker): Boolean = false

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_base_map_layer) {
            val default = PreferenceManager.getProvider()
            val titles = arrayOf(Provider.MAPSFORGE.name, Provider.OSM.name, Provider.DEFAULT.name)
            navigate(
                NavMainGraphDirections.actionToSingleChoiceDialog(
                    RequestsCodes.MAP_STYLE_CODE,
                    R.string.map_style,
                    titles,
                    default
                )
            )
        }
    }

    override fun onCameraIdle() {
        map?.cameraPosition?.also {
            if (previousZoom != it.zoom) {
                handler?.removeCallbacks(this)
                handler?.postDelayed(this, 3500)
                scaleView.show()
                scaleView.update(it.zoom, it.target.latitude)
                previousZoom = it.zoom
            }
        }
    }

    override fun run() = scaleView.hide()

    companion object {
        private const val key_zoom = "zoom"
        private const val key_tilt = "tint"
        private const val key_bearing = "bearing"
        private const val key_target = "target"
    }
}