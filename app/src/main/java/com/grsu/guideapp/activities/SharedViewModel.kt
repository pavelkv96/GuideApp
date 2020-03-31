package com.grsu.guideapp.activities

import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.data.local.PreferenceManager
import com.grsu.guideapp.fragments.setting.PreStart
import com.grsu.guideapp.fragments.setting.Result
import com.grsu.guideapp.views.dialogs.DialogResult

class SharedViewModel : ViewModel(), Runnable {

    private var doubleBackToExitPressedOnce = false
    private val showDrawerLayout: MutableLiveData<Boolean> = MutableLiveData(false)
    private val finishApplication: MutableLiveData<Boolean> = MutableLiveData(false)
    private val handler = Handler()
    private val progress = MutableLiveData<Result>(PreStart)

    fun isPreference() = PreferenceManager.getSplash()

    fun setPreference() = run { PreferenceManager.setSplash(true) }

    fun isShowDrawerLayout() = showDrawerLayout

    fun isFinishApplication() = finishApplication

    fun finishApplication() {
        if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            Toast.makeText(App.getInstance(), R.string.message_once_more_to_exit, Toast.LENGTH_SHORT).show()
            handler.postDelayed(this, 2000)
        } else finishApplication.postValue(true)
    }

    fun showDrawerLayout() = run { showDrawerLayout.value = true }

    override fun run() {
        doubleBackToExitPressedOnce = false
    }

    fun setArgs(result: Result) {
        progress.value = result
    }

    fun getArgs() = progress

    private val choiceDialogResult = MutableLiveData<DialogResult>()

    fun getChoiceDialogResult() = choiceDialogResult

    fun choiceDialogResult(result: DialogResult) {
        choiceDialogResult.value = result
    }

}