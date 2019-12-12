package com.grsu.guideapp.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import com.grsu.guideapp.views.dialogs.MultiChoiceItemsDialogFragment.OnMultiChoiceItemsListener

class SharedViewModel : ViewModel(), OnMultiChoiceItemsListener {

    private val TAG = SharedViewModel::class.java.simpleName

    override fun onCleared() {
        Log.e(TAG, "onCleared: ")
        super.onCleared()
    }

    override fun onOk() {

    }
}