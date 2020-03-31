package com.grsu.guideapp.fragments.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.base.Result
import com.grsu.guideapp.database.content.ContentDataBase
import com.grsu.guideapp.database.content.dao.TypesDao
import com.grsu.guideapp.database.vo.TypeItemVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {
    private val dataBase: ContentDataBase = ContentDataBase.getInstance()
    private val dao: TypesDao
    private val loading = Result.Loading<List<TypeItemVO>>()

    private val typesList = MutableLiveData<Result<List<TypeItemVO>>>()

    init {
        dao = dataBase.typesDao()
        viewModelScope.launch(Dispatchers.IO) {
            typesList.postValue(loading)
//            delay(2000)
            try {
                var list = dao.getAll()
                if (list.isEmpty()) throw KotlinNullPointerException(App.getInstance().getString(R.string.empty_list))

                list = list.filter { it.name.isNotEmpty() && it.image.isNotEmpty() }
                typesList.postValue(Result.Success(list))
            } catch (e: Exception) {
                typesList.postValue(Result.Error(e.message ?: "Some error"))
            }
        }
    }

    fun getData() = typesList
}
