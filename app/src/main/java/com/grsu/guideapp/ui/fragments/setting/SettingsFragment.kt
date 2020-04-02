package com.grsu.guideapp.ui.fragments.setting

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceClickListener
import androidx.preference.PreferenceFragmentCompat
import com.grsu.guideapp.App
import com.grsu.guideapp.NavMainGraphDirections
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.activities.SharedViewModel
import com.grsu.guideapp.project_settings.SharedPref
import com.grsu.guideapp.utils.extensions.navigate
import com.grsu.guideapp.utils.extensions.popBackStack
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener, OnPreferenceClickListener,
    Observer<Result> {

    private var prefLang: ListPreference? = null
    private lateinit var model: SettingsViewModel
    private val sharedModel: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
        model = ViewModelProvider(this)[SettingsViewModel::class.java]
        model.getLiveData().observe(this, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Timber.e("NEW CALLBACK")
            sharedModel.finishApplication()
        }

        prefLang = findPreference(SharedPref.KEY_LANGUAGE)
        prefLang?.apply { summary = entry }

        findPreference<Preference>(SharedPref.KEY_CONTENT)?.onPreferenceClickListener = this
        findPreference<Preference>(SharedPref.KEY_MAP_CONTENT)?.onPreferenceClickListener = this
        findPreference<Preference>(SharedPref.KEY_MAP_DELETE_FILE)?.onPreferenceClickListener = this
        return rootView
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPref: SharedPreferences?, key: String?) {
        key?.also {
            if (SharedPref.KEY_LANGUAGE == it) {
                prefLang?.summary = prefLang?.entry
//                App.getInstance().setLocale(requireContext())
                App.getInstance().setLocale(App.getInstance())
                requireActivity().recreate()
            }
        }
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        preference?.also {
            when (it.key) {
                SharedPref.KEY_CONTENT -> model.clearContentDatabase()
                SharedPref.KEY_MAP_CONTENT -> model.clearCacheDatabase()
                SharedPref.KEY_MAP_DELETE_FILE -> model.deleteAndRestoreMapFile()
            }
        }
        return false
    }

    override fun onChanged(it: Result?) {
        when (it) {
            is Start -> navigate(NavMainGraphDirections.actionToProgressDialog())
            is Update -> sharedModel.setArgs(it)
            is Finish -> popBackStack()
        }
    }
}
