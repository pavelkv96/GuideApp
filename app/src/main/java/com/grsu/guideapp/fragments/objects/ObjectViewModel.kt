package com.grsu.guideapp.fragments.objects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.base.Result
import com.grsu.guideapp.database.content.ContentDataBase
import com.grsu.guideapp.database.vo.ObjectItemVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ObjectViewModel(categoryId: Int) : ViewModel() {
    private val dao = ContentDataBase.getInstance().poiDao()
    private val loading = Result.Loading<List<ObjectItemVO>>()
    private var notFilteredList: List<ObjectItemVO> = listOf()
    private var query: CharSequence = ""

    private val objectsList = MutableLiveData<Result<List<ObjectItemVO>>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            objectsList.postValue(loading)
            try {
                if (categoryId != -1) {
                    notFilteredList = dao.getAllByCatalogId(categoryId, App.getInstance().getString(R.string.locale))
                }
                if (notFilteredList.isEmpty()) throw KotlinNullPointerException(App.getInstance().getString(R.string.empty_list))

                notFilteredList = notFilteredList.filter { it.name.isNotEmpty() }
                objectsList.postValue(Result.Success(notFilteredList))
            } catch (e: Exception) {
                objectsList.postValue(Result.Error(e.message ?: "Some error"))
            }
        }
    }

    fun getData(): MutableLiveData<Result<List<ObjectItemVO>>> = objectsList

    fun search(newQuery: CharSequence) {
        newQuery.trim()

        if (newQuery == query) return

        query = newQuery

        viewModelScope.launch(Dispatchers.IO) {
            delay(300)
            if (newQuery != query) return@launch

            val filteredList = notFilteredList.filter { it.name.contains(query, true) }
            if (filteredList.isEmpty()) objectsList.postValue(Result.Error(App.getInstance().getString(R.string.empty_list)))
            else objectsList.postValue(Result.Success(filteredList))
        }
    }

    fun getQuery(): CharSequence = query
}

@Suppress("UNCHECKED_CAST")
internal class ModelFactory(private val categoryId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == ObjectViewModel::class.java) return ObjectViewModel(categoryId) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.javaClass.simpleName}")
    }
}