package com.grsu.guideapp.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.grsu.guideapp.App
import com.grsu.guideapp.R

object MyLocationLayer {

    fun getMyLocationLayer() = Pair(getMarkerOptions(), getRadiusOptions())

    private fun getMarkerOptions(): MarkerOptions {
        return MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(getBitmap()))
            .flat(true)
            .zIndex(1F)
            .anchor(0.5F, 0.5F)
            .position(LatLng(0.0, 0.0))
    }

    private fun getRadiusOptions(): CircleOptions {
        return CircleOptions()
            .center(LatLng(0.0, 0.0))
            .zIndex(1F)
            .strokeWidth(1F)
            .fillColor(Color.parseColor("#55DCE5F4"))
            .strokeColor(Color.parseColor("#97BAF4"))
            .radius(10.0)
            .clickable(false)
    }

    private fun getBitmap(): Bitmap {
        var drawable = ContextCompat.getDrawable(App.getInstance(), R.drawable.ic_navigation)!!
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, Color.RED)
        drawable = DrawableCompat.unwrap<Drawable>(drawable)
        val canvas = Canvas()

        return with(drawable) {
            val bmp = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            canvas.setBitmap(bmp)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            draw(canvas)
            bmp
        }
    }
}