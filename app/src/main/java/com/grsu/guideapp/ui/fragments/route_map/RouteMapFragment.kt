package com.grsu.guideapp.ui.fragments.route_map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.map.BaseMapFragment
import com.grsu.guideapp.service.LocationUpdatesService
import com.grsu.guideapp.ui.activities.MainActivity
import com.grsu.guideapp.ui.fragments.route_details.RouteDetailsFragmentArgs
import com.grsu.guideapp.utils.CheckPermission
import com.grsu.guideapp.utils.extensions.*
import timber.log.Timber

class RouteMapFragment : BaseMapFragment(), OnReceiverResponse {

    companion object {
        private const val CODE_LOCATION_PERMISSION = 3
        private const val key_bottom_sheet = "bottom_sheet"
    }

    private var myPosition: Marker? = null
    private var myPositionRadius: Circle? = null

    override var resMap: Int? = R.layout.fragment_route_details
    private lateinit var title: MaterialTextView
    private lateinit var description: MaterialTextView
    private lateinit var duration: MaterialTextView
    private lateinit var distance: MaterialTextView
    private lateinit var args: RouteDetailsFragmentArgs
    private lateinit var fullscreen: FloatingActionButton
    private lateinit var likeButton: FloatingActionButton
    private lateinit var myLocationButton: FloatingActionButton
    private lateinit var places: FloatingActionButton
    private lateinit var bottomSheetContainer: NestedScrollView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var factory: ModelFactory
    private val model: RouteMapViewModel by viewModels { factory }
    private var receiver: MyReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = RouteDetailsFragmentArgs.fromBundle(requireArguments())
        factory = ModelFactory(args.routeId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.also {
            title = it.findViewById(R.id.mtv_fragment_route_details_title)
            description = it.findViewById(R.id.mtv_fragment_route_details_description)
            duration = it.findViewById(R.id.mtv_fragment_route_details_duration)
            distance = it.findViewById(R.id.mtv_fragment_route_details_distance)

            places = it.findViewById(R.id.fab_base_map_places)
            fullscreen = it.findViewById(R.id.fab_base_map_fullscreen)
            likeButton = it.findViewById(R.id.fab_fragment_route_details_like)
            myLocationButton = it.findViewById(R.id.fab_base_map_my_location)
            bottomSheetContainer = it.findViewById(R.id.bottom_sheet)
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        }
        title.text = args.title
        likeButton.visibility = View.GONE
        likeButton.setOnClickListener(this)

        myLocationButton.show()
        myLocationButton.setOnClickListener(this)

        fullscreen.show()
        fullscreen.setOnClickListener(this)
        model.getIsFullscreenLiveData().observe(this, Observer { state ->
            fullscreen.setImageResource(if (state) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen)
            val newState = if (state) BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.state = newState
            likeButton.visibility = if (state) View.GONE else View.VISIBLE
        })
        places.show()
        places.setOnClickListener(this)

        model.getLocationPermission().observe(this, Observer { state ->
            val icon = if (!state.availability && state.location == null) {
                R.drawable.ic_location_off
            } else if (!state.availability && state.location != null) {
                R.drawable.ic_location_off
            } else if (state.availability && state.location == null) {
                R.drawable.ic_unknow_location
            } else R.drawable.ic_my_location

            myLocationButton.tag = icon
            myLocationButton.setImageResource(icon)

            state.location?.let {
                myPosition?.isVisible = true
                myPositionRadius?.isVisible = true

                myPosition?.position = it.toLatLng()
                myPosition?.rotation = it.bearing
                myPositionRadius?.center = it.toLatLng()
                myPositionRadius?.radius = it.accuracy.toDouble()
            }
        })
        model.getDetailsLiveData().observe(this, Observer {
            if (it != null) {
                duration.text = it.duration.toDuration()
                distance.text = it.distance.toDistance()
                description.text = it.description
            }
        })

        val defaultState = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = savedInstanceState?.getInt(key_bottom_sheet, defaultState) ?: defaultState
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        model.setFullscreen(true)
                        likeButton.visibility = View.GONE
                    }
                    else -> {
                        if (likeButton.isShown.not()) likeButton.visibility = View.VISIBLE
                    }
                }
            }
        })
        return view
    }

    override fun onMapReady(_map: GoogleMap) {
        super.onMapReady(_map)

        model.getBoundsLiveData().observe(this, Observer { bounds->
            bounds?.let { map?.animateCamera(CameraUpdateFactory.newLatLngBounds(it, 50)) }
        })

        myPosition = map?.addMarker(myLocation?.first)
        myPosition?.isVisible = false
        myPositionRadius = map?.addCircle(myLocation?.second)
        myPositionRadius?.isVisible = false
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as? MainActivity)?.getAppBarLayout()?.hide()
        if (CheckPermission.checkLocationPermission(App.getInstance())) {
            receiver = MyReceiver(this)
            receiver?.also {
                requireActivity().startService(Intent(App.getInstance(), LocationUpdatesService::class.java))
                val filter = IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
                LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(it, filter)
            }
            model.setLocationPermission(true)
        } else {
            model.setLocationPermission(false)
        }
    }

    override fun onStop() {
        (requireActivity() as? MainActivity)?.getAppBarLayout()?.show()
        requireActivity().stopService(Intent(App.getInstance(), LocationUpdatesService::class.java))
        receiver?.also { LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(it) }
        receiver = null
        super.onStop()
    }

    override fun onDestroyView() {
        myPosition?.remove()
        myPositionRadius?.remove()
        myPosition = null
        myPositionRadius = null
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_LOCATION_PERMISSION && grantResults[0] == 0) {
            receiver = null
            receiver = MyReceiver(this)
            receiver?.also {
                requireActivity().startService(Intent(App.getInstance(), LocationUpdatesService::class.java))
                val filter = IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
                LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(it, filter)
            }
            model.setLocationPermission(true)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.fab_base_map_my_location -> locationAction(v)
            R.id.fab_fragment_route_details_like -> Timber.e("Click")
            R.id.fab_base_map_fullscreen -> model.setFullscreen()
            R.id.fab_base_map_places -> navigate(RouteMapFragmentDirections.actionRouteMapToOrder())
        }
    }

    override fun onReceiver(intent: Intent) {
        intent.extras?.get(LocationUpdatesService.EXTRA_LOCATION)?.let {
            when (it) {
                is Boolean -> model.setLocationPermission(it)
                is Location -> model.setLocation(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(key_bottom_sheet, bottomSheetBehavior.state)
        super.onSaveInstanceState(outState)
    }

    private fun locationAction(v: View) {
        if (CheckPermission.checkLocationPermission(App.getInstance()).not()) {
            requestPermissions(CheckPermission.groupLocation, CODE_LOCATION_PERMISSION)
            return
        }

        val text = when (v.tag) {
            R.drawable.ic_my_location -> {
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition?.position, 16F))
                "My Location"
            }
            R.drawable.ic_unknow_location -> "Unknown location"
            else -> "Can't access to location"
        }
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}

interface OnReceiverResponse {
    fun onReceiver(intent: Intent)
}

class MyReceiver(private val listener: OnReceiverResponse) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?): Unit = run { intent?.let { listener.onReceiver(it) } }
}