package com.grsu.guideapp.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.extensions.show
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var appBar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private var appBarConfiguration: AppBarConfiguration? = null
    private lateinit var model: SharedViewModel

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(App.getInstance().setLocale(newBase))

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        model = ViewModelProvider(this)[SharedViewModel::class.java]
        appBar = findViewById(R.id.appbar)

        Timber.e("onCreate: ")
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBar.show()
        model.isShowDrawerLayout().observe(this, Observer {
            if (it) {
                navController.setGraph(R.navigation.nav_main_graph)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                appBarConfiguration = AppBarConfiguration(
                    setOf(R.id.nav_routes, R.id.nav_catalogs, R.id.nav_settings, R.id.nav_about),
                    drawerLayout
                )
                appBarConfiguration?.also { configuration ->
                    setupActionBarWithNavController(navController, configuration)
                    navView.setupWithNavController(navController)
                }
            }
        })

        model.isFinishApplication().observe(this, Observer { if (it) finish() })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return appBarConfiguration?.let {
            navController.navigateUp(it)
        } ?: super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        appBarConfiguration?.drawerLayout?.apply {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
                return
            }
        }

        super.onBackPressed()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.setTo(baseContext.resources.configuration)
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    fun getAppBarLayout() = appBar
}