@file:Suppress("deprecation")

package com.grsu.guideapp.ui.custom.dialogs

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grsu.guideapp.ui.activities.SharedViewModel
import com.grsu.guideapp.ui.fragments.setting.Result
import com.grsu.guideapp.ui.fragments.setting.Update
import timber.log.Timber

class ProgressDialogFragment : DialogFragment(), Observer<Result> {

    private lateinit var process: ProgressDialog
    private lateinit var sharedModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedModel.getArgs().observe(this, this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        process = ProgressDialog(requireContext())
        return process
    }

    override fun onChanged(result: Result?) {
        when (result) {
            /*is Start -> {
                process.setMessage(result.message)
                process.setTitle(result.title)
//                process.setCancelable(false)
            }*/
            is Update -> {
                process.setMessage(result.message)
                process.setTitle(result.title)
//                process.setCancelable(false)
            }
        }
        Timber.e("onChanged: ${if (result != null) { result::class.java.simpleName } else null}")
    }
}