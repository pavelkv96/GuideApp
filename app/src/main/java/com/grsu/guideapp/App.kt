package com.grsu.guideapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.room.Room
import com.grsu.guideapp.database.content.ContentDataBase
import com.grsu.guideapp.database.content.migrations.MyCallBack
import com.grsu.guideapp.project_settings.SharedPref
import com.grsu.guideapp.utils.extensions.getCurrentLocale
import java.util.*

@Suppress("unused")
class App : Application() {

    companion object {
        private lateinit var app: App

        @JvmStatic
        fun getInstance(): App {
            return app
        }

        @JvmStatic
        fun getThread(): AppExecutors {
            return AppExecutors.INSTANCE
        }

        @JvmStatic
        fun isOnline(): Boolean {
            val mn = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = mn.activeNetworkInfo
            return activeNetwork?.isConnected == true
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        val dataBase = Room.databaseBuilder(this, ContentDataBase::class.java, "content.db")
//            .createFromAsset("content.db")
            .allowMainThreadQueries()
            .addCallback(MyCallBack())
            .build()

        dataBase.routesDao().getRoutes()

        dataBase.close()
    }

    @JvmName("isNewOnline")
    fun isOnline(): Boolean {
        val mn = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = mn.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    fun setLocale(pref: SharedPreferences): Context {
        if (!pref.contains(SharedPref.KEY_LANGUAGE)) {
            val locale = Locale.getDefault().getCurrentLocale()
            pref.edit().putString(SharedPref.KEY_LANGUAGE, locale).apply()
        }

        val conf = resources.configuration

        val loc = pref.getString(SharedPref.KEY_LANGUAGE, resources.getString(R.string.locale))
        val locale = Locale(loc)

        conf.setLocale(locale)
        return createConfigurationContext(conf)
    }
}