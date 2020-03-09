package com.grsu.guideapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.preference.PreferenceManager
import com.grsu.guideapp.project_settings.SharedPref
import com.grsu.guideapp.utils.extensions.getCurrentLocale
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Suppress("unused")
class App : Application() {

    private lateinit var executors: Executor

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
    }

    override fun attachBaseContext(base: Context) = super.attachBaseContext(setLocale(base))

    override fun onCreate() {
        super.onCreate()
        app = this
        executors = Executors.newFixedThreadPool(4)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        dataBase.routesDao().getRoutes()

        dataBase.close()
    }

    fun isOnline(): Boolean {
        val mn = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = mn.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    @Suppress("DEPRECATION")
    fun setLocale(context: Context): Context {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        if (!pref.contains(SharedPref.KEY_LANGUAGE)) {
            val locale = Locale.getDefault().getCurrentLocale()
            pref.edit().putString(SharedPref.KEY_LANGUAGE, locale).apply()
        }

        val loc = pref.getString(SharedPref.KEY_LANGUAGE, "en")
        val locale = Locale(loc)
        Locale.setDefault(locale)

        val conf = context.resources.configuration
        conf.setLocale(locale)
        context.resources.updateConfiguration(conf, context.resources.displayMetrics)
        return context
    }

    fun getExecutor() = executors

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setLocale(this)
    }
}