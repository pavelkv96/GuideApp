package com.grsu.guideapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.grsu.guideapp.R
import timber.log.Timber

class LocationUpdatesService : Service() {
    companion object {
        private const val PACKAGE_NAME = "com.grsu.location"
        const val ACTION_BROADCAST: String = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION: String = "$PACKAGE_NAME.location"
    }

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback


    override fun onCreate() {
        super.onCreate()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult?.lastLocation)
            }
        }

        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000L
        mLocationRequest.fastestInterval = 2000L
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val mChannel = NotificationChannel("channel_01", name, NotificationManager.IMPORTANCE_DEFAULT)
            val mNotificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java)
            mNotificationManager?.createNotificationChannel(mChannel)
        }

        Timber.i("Requesting location updates")
        try {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission. Could not request updates. $unlikely")
        }
    }

    override fun onBind(intent: Intent?): IBinder?  = null

    private fun onNewLocation(location: Location?) {
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onDestroy() {
        Timber.i("Removing location updates")
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permission. Could not request updates. $unlikely")
        }
        super.onDestroy()
    }
}