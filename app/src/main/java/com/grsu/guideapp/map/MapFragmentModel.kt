package com.grsu.guideapp.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.Tile
import com.grsu.guideapp.App
import com.grsu.guideapp.data.local.PreferenceManager
import com.grsu.guideapp.database.cache.CacheDataBase
import com.grsu.guideapp.database.cache.dao.CacheDao
import com.grsu.guideapp.project_settings.RequestsCodes
import com.grsu.guideapp.views.dialogs.DialogResult
import com.grsu.guideapp.views.dialogs.TypeDialog
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import java.io.File

class MapFragmentModel(file: File, provider: Provider) : ViewModel() {

    private val dataBase: CacheDataBase = CacheDataBase.getInstance()
    private val dao: CacheDao
    private var updateStyle = MutableLiveData(provider)
    private var tileAdapter: TileAdapter?

    init {
        dao = dataBase.cache()
        AndroidGraphicFactory.createInstance(App.getInstance())
        tileAdapter = TileAdapter(file, provider, dao)
    }

    private fun setProvider(provider: Provider) {
        if (provider == updateStyle.value) return

        updateStyle.value = provider
        tileAdapter?.setProvider(provider)
    }

    fun getProvider() = updateStyle

    fun getBoxingBounds() = tileAdapter?.getBoundingBox()

    fun loadTile(x: Int, y: Int, zoom: Int): Tile? = tileAdapter?.loadTile(x, y, zoom)

    override fun onCleared() {
        tileAdapter?.dispose()
        tileAdapter = null
        AndroidGraphicFactory.clearResourceMemoryCache()
        super.onCleared()
    }

    fun setChoiceDialog(result: DialogResult?) {
        if (result == null) return

        if (result.id == RequestsCodes.MAP_STYLE_CODE && result.type == TypeDialog.SINGLE) {
            if (result.result.first() != updateStyle.value!!.name) {
                val provider = Provider.valueOf(result.result.first())
                PreferenceManager.setProvider(provider)
                setProvider(provider)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal class ModelFactory(private val file: File, private val provider: Provider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == MapFragmentModel::class.java) return MapFragmentModel(file, provider) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.javaClass.simpleName}")
    }
}