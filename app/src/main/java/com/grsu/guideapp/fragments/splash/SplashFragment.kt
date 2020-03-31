package com.grsu.guideapp.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.grsu.guideapp.R
import com.grsu.guideapp.activities.SharedViewModel
import com.grsu.guideapp.base.Result
import com.grsu.guideapp.utils.CheckPermission
import com.grsu.guideapp.utils.extensions.*

class SplashFragment : Fragment(), View.OnClickListener {

    private val sharedModel: SharedViewModel by activityViewModels()
    private val model: SplashViewModel by viewModels()

    private lateinit var progressView: ProgressBar

    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView

    private lateinit var nextView: MaterialButton
    private lateinit var exitView: MaterialButton
    private lateinit var settingsView: MaterialButton

    companion object {
        private const val REQUEST_CODE: Int = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        if (sharedModel.isPreference()) {
            navigate(R.id.action_splash_to_routes)
            sharedModel.showDrawerLayout()
            return view
        }

        with(view) {
            nextView = findViewById(R.id.btn_fragment_splash_next)
            exitView = findViewById(R.id.btn_fragment_splash_close)
            settingsView = findViewById(R.id.btn_fragment_splash_settings)

            titleView = findViewById(R.id.tv_fragment_splash_title)
            descriptionView = findViewById(R.id.tv_fragment_splash_description)

            progressView = findViewById(R.id.pb_fragment_splash_progress)
        }

        nextView.setOnClickListener(this)
        exitView.setOnClickListener(this)
        settingsView.setOnClickListener(this)

        model.getProgress().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    navigate(R.id.action_splash_to_routes)
                    sharedModel.showDrawerLayout()
                    sharedModel.setPreference()
                }
                is Result.Loading -> {
                    if (it.isPreload) {
                        progressView.show()
                        progressView.progress = it.progress
                    } else progressView.hide()
                }
                is Result.Error -> {
                    Snackbar.make(nextView, it.error, Snackbar.LENGTH_LONG).show()
                    nextView.hide()
                }
            }
        })
        return view
    }

    override fun onStart() {
        super.onStart()
        nextView.show()
        settingsView.hide()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_fragment_splash_settings -> requireActivity().goToSettings()
            R.id.btn_fragment_splash_close -> requireActivity().finish()
            R.id.btn_fragment_splash_next -> requestPermissions(REQUEST_CODE to CheckPermission.allPermission)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && CheckPermission.checkStoragePermission(requireContext())) {
            nextView.isEnabled = false
            model.copyFromAssets()
        } else {
            settingsView.show()
            nextView.hide()
            titleView.setText(R.string.provide_access)
            descriptionView.setText(R.string.provide_access_description)
        }
    }
}