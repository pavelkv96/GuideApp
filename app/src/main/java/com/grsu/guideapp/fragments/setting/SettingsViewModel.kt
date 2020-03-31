package com.grsu.guideapp.fragments.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grsu.guideapp.data.local.PreferenceManager
import com.grsu.guideapp.fragments.setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel : ViewModel() {

    private val loading = MutableLiveData<Result>(PreStart)

    fun getLiveData() = loading

    fun clearContentDatabase() {
//                    val file = File(Settings.CACHE, Settings.CACHE_DATABASE_NAME)
//                    if (file.exists()) {
//                        Toast.make(activity, R.string.success_database_deleted, Toast.LENGTH_SHORT)
//                    } else {
//                        Toast.make(activity, R.string.error_database_not_found, Toast.LENGTH_SHORT)
//                    }
        viewModelScope.launch {
            loading.value = Start
            loading.value = Update("Title", "Message")
            withContext(Dispatchers.IO) {
                //TODO("clear content database")
                //ContentRepository.clearAllTables()
                delay(2000)
                loading.postValue(
                    Update(
                        "Title new",
                        "Message new"
                    )
                )
                //TODO("restore content database")
                delay(2000)
            }
            PreferenceManager.setSplash(false)

            loading.value = Finish
            loading.value = PreStart
        }
    }

    fun deleteAndRestoreMapFile() {
//                  val file = File(StorageUtils.getDatabasePath(), Settings.MAP_FILE)
        viewModelScope.launch {
            loading.value = Start
            loading.value = Update("", "delete map file")
            withContext(Dispatchers.IO) {
                delay(2000)
                loading.postValue(
                    Update(
                        "",
                        "getting new version map file"
                    )
                )
                delay(2000)
            }
            loading.value = Finish
            loading.value = PreStart
        }
    }

    fun clearCacheDatabase() {
        viewModelScope.launch {
            loading.value = Start
            loading.value = Update("", "clear cache database")
            withContext(Dispatchers.IO){
//                TODO("clear cache database")
                delay(2000)
            }
            loading.value = Finish
            loading.value = PreStart
        }
    }
}
