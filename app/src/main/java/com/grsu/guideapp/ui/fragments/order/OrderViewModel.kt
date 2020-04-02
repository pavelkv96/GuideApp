package com.grsu.guideapp.ui.fragments.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.base.Result
import com.grsu.guideapp.data.local.database.content.ContentDataBase
import com.grsu.guideapp.data.local.database.content.dao.TypesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val dataBase: ContentDataBase = ContentDataBase.getInstance()
    private val dao: TypesDao

    private val loading = Result.Loading<List<CheckedItem>>()
    private val orderLiveData = MutableLiveData<Result<List<CheckedItem>>>(loading)

    init {
        dao = dataBase.typesDao()
        viewModelScope.launch(Dispatchers.IO) {
            orderLiveData.postValue(loading)
            try {
                val list = dao.getAll()
                if (list.isEmpty()) throw NullPointerException(App.getInstance().getString(R.string.empty_list))
                val filteredList = list.filter { it.name.isNotEmpty() && it.image.isNotEmpty() }.map { CheckedItem(it) }
                if (filteredList.isEmpty()) throw NullPointerException(App.getInstance().getString(R.string.empty_list))
                orderLiveData.postValue(Result.Success(filteredList))
            } catch (e: Exception) {
                orderLiveData.postValue(Result.Error(e.message ?: "Some error"))
            }
        }
    }

    fun getListLiveData() = orderLiveData
}
