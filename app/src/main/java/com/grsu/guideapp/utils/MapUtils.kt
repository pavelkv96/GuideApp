package com.grsu.guideapp.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.grsu.guideapp.utils.extensions.toLocation
import org.mapsforge.core.model.Tile
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object MapUtils {
    private const val mMaxZoomLevel = 29
    private const val mModulo = 1 shl mMaxZoomLevel

    @JvmStatic
    fun getTileIndex(pX: Int, pY: Int, pZoom: Int): Long {
        return (pZoom.toLong() shl mMaxZoomLevel * 2) + (pX.toLong() shl mMaxZoomLevel) + pY.toLong()
    }

    @JvmStatic
    @Deprecated(message = "Not using this method")
    fun getTile(pMapTileIndex: Long, tileSize: Int): Tile {
        return Tile(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex).toByte(), tileSize)
    }

    @JvmStatic
    fun getTile(x: Int, y: Int, zoom: Int, tileSize: Int) = Tile(x, y, zoom.toByte(), tileSize)

    @JvmStatic
    fun getIndex(pMapTileIndex: Long) = getIndex(getX(pMapTileIndex), getY(pMapTileIndex), getZoom(pMapTileIndex))

    @JvmStatic
    fun isMoreDistance(distance: Float, currentLocation: Location, latLng: LatLng): Boolean {
        return distance > currentLocation.distanceTo(latLng.toLocation())
    }

    @JvmStatic
    @Throws(exceptionClasses = [NullPointerException::class])
    fun isContains(latLng: LatLng, border: LatLngBounds) {
        if (!border.contains(latLng)) {
            throw NullPointerException("Don't contains")
        }
    }

    private fun getX(tileIndex: Long) = ((tileIndex shr mMaxZoomLevel) % mModulo).toInt()

    private fun getY(tileIndex: Long) = (tileIndex % mModulo).toInt()

    private fun getZoom(tileIndex: Long) = (tileIndex shr mMaxZoomLevel * 2).toInt()

    private fun getIndex(x: Int, y: Int, zoom: Int): Long {
        return (((zoom shl zoom) + x shl zoom) + y).toLong()
    }

    internal fun computeAngleBetween(from: LatLng, to: LatLng): Double {
        return distanceRadians(
            toRadians(from.latitude), toRadians(from.longitude),
            toRadians(to.latitude), toRadians(to.longitude)
        )
    }

    private fun distanceRadians(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2))
    }

    private fun arcHav(x: Double): Double = 2 * asin(sqrt(x))

    private fun havDistance(lat1: Double, lat2: Double, dLng: Double): Double {
        return hav(lat1 - lat2) + hav(dLng) * cos(lat1) * cos(lat2)
    }

    private fun hav(x: Double): Double = sqrt(sin(x * 0.5))
}
