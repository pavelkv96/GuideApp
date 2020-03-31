package com.grsu.guideapp.fragments.object_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.base.Result
import com.grsu.guideapp.database.content.ContentDataBase
import com.grsu.guideapp.database.vo.DetailsObjectVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ObjectDetailsModel(private val objectId: Int) : ViewModel() {

    private val dataBase = ContentDataBase.getInstance()
    private val poiDao = dataBase.poiDao()
    private val referencesDao = dataBase.referencesDao()

    private val isLikedLiveData = MutableLiveData(false)
    private val contentObject = Result.Loading<DetailsObjectVO.ObjectVO>()
    private val imagesObject = Result.Loading<List<DetailsObjectVO.ImagesVO>>()
    private val otherContent = Result.Loading<List<ContentItem>>()
    private val otherLiveData = MutableLiveData<Result<List<ContentItem>>>(otherContent)
    private val listImages = MutableLiveData<Result<List<DetailsObjectVO.ImagesVO>>>(imagesObject)
    private val objectContent = MutableLiveData<Result<DetailsObjectVO.ObjectVO>>(contentObject)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val objectVO = poiDao.getObjectById(objectId, App.getInstance().getString(R.string.locale))


                val icons = listOf(
                    R.drawable.ic_favorite_add,
                    R.drawable.ic_favorite_remove,
                    R.drawable.ic_fullscreen,
                    R.drawable.ic_layers
                )
                val actions = App.getInstance().resources.getStringArray(R.array.object_details_other_actions)
                val content = listOf(objectVO.address, objectVO.phone, objectVO.email, objectVO.link)

                val map = content.mapIndexedNotNull { i, value ->
                    if (value == null) null else ContentItem(i + 1, value, actions[i], icons[i])
                }

                objectContent.postValue(Result.Success(objectVO))
                otherLiveData.postValue(Result.Success(map))
            } catch (e: Exception) {
                objectContent.postValue(Result.Error(e.message ?: "Some error"))
            }

            try {
                val objectVO = referencesDao.getReferencesByObjectId(objectId)
                listImages.postValue(Result.Success(objectVO))
            } catch (e: Exception) {
                listImages.postValue(Result.Error(e.message ?: "Some error"))
            }
        }
    }

    fun isLikedLiveDate() = isLikedLiveData

    fun setLike() {
        isLikedLiveData.value = isLikedLiveData.value!!.not()
        viewModelScope.launch(Dispatchers.IO) {
            objectId
        }
    }

    fun listImage(): MutableLiveData<Result<List<DetailsObjectVO.ImagesVO>>> = listImages

    fun content(): MutableLiveData<Result<DetailsObjectVO.ObjectVO>> = objectContent

    fun otherContent(): MutableLiveData<Result<List<ContentItem>>> = otherLiveData
}

@Suppress("UNCHECKED_CAST")
internal class ModelFactory(private val poiId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == ObjectDetailsModel::class.java) return ObjectDetailsModel(poiId) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.javaClass.simpleName}")
    }
}