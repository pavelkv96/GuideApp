package com.grsu.guideapp.data.local

import androidx.preference.PreferenceManager
import com.grsu.guideapp.App
import com.grsu.guideapp.map.Provider
import com.grsu.guideapp.utils.extensions.edit

object PreferenceManager {
    private val preference = PreferenceManager.getDefaultSharedPreferences(App.getInstance())
    private const val key_layer = "layer"
    private const val key_splash = "splash"

    fun getProvider(): String = preference.getString(key_layer, Provider.DEFAULT.name) ?: Provider.DEFAULT.name

    fun setProvider(provider: Provider) = preference.edit { putString(key_layer, provider.name) }

    fun getSplash(): Boolean = preference.getBoolean(key_splash, false)

    fun setSplash(isShow: Boolean) = preference.edit { putBoolean(key_splash, isShow) }
}