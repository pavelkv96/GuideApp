package com.grsu.guideapp.ui.fragments.route_details

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.content.ContentDataBase
import com.grsu.guideapp.data.local.database.content.dao.LinesDao
import com.grsu.guideapp.data.local.database.content.dao.PoiDao
import com.grsu.guideapp.data.local.database.content.dao.RoutesDao
import com.grsu.guideapp.data.local.database.vo.RouteDetailsVO
import com.grsu.guideapp.service.LocationState
import com.grsu.guideapp.utils.CryptoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteDetailsViewModel(id: Int) : ViewModel() {

    private val dataBase = ContentDataBase.getInstance()
    private val routesDao: RoutesDao = dataBase.routesDao()
    private val linesDao: LinesDao = dataBase.linesDao()
    private val poi: PoiDao = dataBase.poiDao()
    private val details: MutableLiveData<RouteDetailsVO?> = MutableLiveData()
    private val locationPermission: MutableLiveData<LocationState> = MutableLiveData(LocationState(false, null))
    private val isFullscreen: MutableLiveData<Boolean> = MutableLiveData(false)
    private val bounds: MutableLiveData<LatLngBounds?> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val routeDetails = routesDao.getRouteDetails(id, App.getInstance().getString(R.string.locale))
            details.postValue(routeDetails)
            val bound = CryptoUtils.decodeP(routeDetails.southwest) to CryptoUtils.decodeP(routeDetails.northeast)
            bounds.postValue(LatLngBounds(bound.first, bound.second))
            linesDao.getLines(id)
            poi.getPoiByIdRoute(id, App.getInstance().getString(R.string.locale))
        }
    }

    fun getBoundsLiveData() = bounds

    fun getDetailsLiveData() = details

    fun getIsFullscreenLiveData() = isFullscreen

    fun getLocationPermission() = locationPermission

    fun setLocationPermission(isGranted: Boolean) {
        locationPermission.value = locationPermission.value?.also { it.availability = isGranted }
    }

    fun setLocation(location: Location) {
        locationPermission.value = locationPermission.value?.also { it.location = location }
    }

    fun setFullscreen() {
        isFullscreen.value = isFullscreen.value?.not() ?: false
    }

    fun setFullscreen(isFull: Boolean) {
        isFullscreen.value = isFull
    }
}

@Suppress("UNCHECKED_CAST")
internal class ModelFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == RouteDetailsViewModel::class.java) return RouteDetailsViewModel(id) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.javaClass.simpleName}")
    }
}