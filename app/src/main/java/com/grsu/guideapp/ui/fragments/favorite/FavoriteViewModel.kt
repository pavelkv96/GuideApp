package com.grsu.guideapp.ui.fragments.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.utils.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel() : ViewModel() {

    private val result: Result<List<String>> = Result.Loading()
    private val favoriteLiveData: MutableLiveData<Result<List<String>>> = MutableLiveData(result)

    fun getFavoriteLiveData() = favoriteLiveData

    fun getFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteLiveData.postValue(Result.Loading())

            val list = mutableListOf<String>()
            for (i in 1..50) {
                list.add("Some name $i")
            }

            favoriteLiveData.postValue(Result.Success(list))
        }
    }
}