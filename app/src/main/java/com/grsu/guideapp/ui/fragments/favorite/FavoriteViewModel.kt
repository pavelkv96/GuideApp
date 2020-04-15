package com.grsu.guideapp.ui.fragments.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.database.content.ContentDataBase
import com.grsu.guideapp.data.local.database.vo.FavoriteVO
import com.grsu.guideapp.utils.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val database = ContentDataBase.getInstance()
    private val routeDao = database.routesDao()
    private val poiDao = database.poiDao()
    private val result: Result<List<FavoriteVO>> = Result.Loading()
    private val favoriteLiveData: MutableLiveData<Result<List<FavoriteVO>>> = MutableLiveData(result)

    fun getFavoriteLiveData() = favoriteLiveData

    fun getFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteLiveData.postValue(Result.Loading())
            val routes = routeDao.getFavouriteRoutes(App.getInstance().getString(R.string.locale))
            val poi = poiDao.getFavouritePoi(App.getInstance().getString(R.string.locale))
            val result = (poi + routes).sortedBy { it.name }
            /*val list = mutableListOf<String>()
            for (i in 1..50) {
                list.add("Some name $i")
            }*/

            favoriteLiveData.postValue(Result.Success(result))
        }
    }
}