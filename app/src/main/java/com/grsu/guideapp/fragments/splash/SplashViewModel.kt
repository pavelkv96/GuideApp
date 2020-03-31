package com.grsu.guideapp.fragments.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.App
import com.grsu.guideapp.base.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("UNUSED")
class SplashViewModel : ViewModel() {
    private val repository = SplashRepository()

    private val app = App.getInstance()
    private val loading = Result.Loading<Boolean>()
    private val progress: MutableLiveData<Result<Boolean>> = MutableLiveData(loading)

    fun getProgress() = progress

    fun copyFromAssets() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.isPreload = true
            progress.postValue(loading)
            repository.parseData()
            loading.progress = 24

            progress.postValue(loading)
            repository.copyDatabase()
            loading.progress = 72

            progress.postValue(loading)
            repository.copyPhotoFromAssets()
            loading.progress = 100
            loading.isPreload = false

            progress.postValue(loading)
            //TODO set in preference true if successful else false

            progress.postValue(Result.Success(true))
//            progress.postValue(Result.Error("Some error"))
        }
    }
}