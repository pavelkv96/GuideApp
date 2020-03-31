package com.grsu.guideapp.fragments.tabs.all_routes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.database.content.ContentDataBase
import com.grsu.guideapp.database.vo.RouteItemVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllRoutesViewModel : ViewModel() {
    private val dataBase = ContentDataBase.getInstance()

    private val dao = dataBase.routesDao()
    private var data = MutableLiveData<List<RouteItemVO>>(mutableListOf())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val language = App.getInstance().getString(R.string.locale)
            val routes = dao.getNewRoutes(language)
            data.postValue(routes)
        }
    }

    fun getData() = data
}