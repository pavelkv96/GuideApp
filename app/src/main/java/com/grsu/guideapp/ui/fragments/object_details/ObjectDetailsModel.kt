package com.grsu.guideapp.ui.fragments.object_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.base.Result
import com.grsu.guideapp.data.local.database.content.ContentDataBase
import com.grsu.guideapp.data.local.database.vo.ContentVO
import com.grsu.guideapp.data.local.database.vo.ImagesVO
import com.grsu.guideapp.data.local.database.vo.ObjectVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ObjectDetailsModel(private val objectId: Int) : ViewModel() {

    private val dataBase = ContentDataBase.getInstance()
    private val poiDao = dataBase.poiDao()
    private val referencesDao = dataBase.referencesDao()

    private val isLikedLiveData = MutableLiveData(false)
    private val objectContent = Result.Loading<ObjectVO>()
    private val textContent = Result.Loading<ContentVO>()
    private val imagesObject = Result.Loading<List<ImagesVO>>()
    private val otherContent = Result.Loading<List<ContentItem>>()
    private val otherLiveData = MutableLiveData<Result<List<ContentItem>>>(otherContent)
    private val textContentLiveData = MutableLiveData<Result<ContentVO>>(textContent)
    private val listImages = MutableLiveData<Result<List<ImagesVO>>>(imagesObject)
    private val objectContentLiveData = MutableLiveData<Result<ObjectVO>>(objectContent)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val objectVO = poiDao.getObjectById(objectId)
                val contentVO = poiDao.getDescriptionById(objectId, App.getInstance().getString(R.string.locale))


                val icons = listOf(
                    R.drawable.ic_map,
                    R.drawable.ic_phone,
                    R.drawable.ic_email,
                    R.drawable.ic_link
                )
                val actions = App.getInstance().resources.getStringArray(R.array.object_details_other_actions)
                val content = listOf(objectVO.address, objectVO.phone, objectVO.email, objectVO.link)

                isLikedLiveData.postValue(objectVO.is_favorite)

                val map = content.mapIndexedNotNull { i, value ->
                    if (value == null) null else ContentItem(i + 1, value, actions[i], icons[i])
                }

                textContentLiveData.postValue(Result.Success(contentVO))
                objectContentLiveData.postValue(Result.Success(objectVO))
                otherLiveData.postValue(Result.Success(map))
            } catch (e: Exception) {
                objectContentLiveData.postValue(Result.Error(e.message ?: "Some error"))
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
        viewModelScope.launch(Dispatchers.IO) {
            val value = objectContentLiveData.value
            if (value is Result.Success) {
                val isLike = isLikedLiveData.value!!.not()
                value.data.is_favorite = isLike
                val row = poiDao.changeFavoriteState(value.data)
//                val row = 1
                Timber.e("Changed count row: $row")
                isLikedLiveData.postValue(isLike)
            }
        }
    }

    fun textLiveDate() = textContentLiveData

    fun listImage(): MutableLiveData<Result<List<ImagesVO>>> = listImages

    fun content(): MutableLiveData<Result<ObjectVO>> = objectContentLiveData

    fun otherContent(): MutableLiveData<Result<List<ContentItem>>> = otherLiveData
}

@Suppress("UNCHECKED_CAST")
internal class ModelFactory(private val poiId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == ObjectDetailsModel::class.java) return ObjectDetailsModel(poiId) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.javaClass.simpleName}")
    }
}