package com.grsu.guideapp.ui.fragments.tabs

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.fragments.tabs.my_routes.MyRoutesFragment
import com.grsu.guideapp.ui.fragments.tabs.all_routes.AllRoutesFragment

class RouteTabViewModel : ViewModel() {

    private val listFragment: MutableLiveData<List<Pair<Fragment, String>>> = MutableLiveData(listOf())

    init {
        val fragments = listOf<Pair<Fragment, String>>(
            MyRoutesFragment.newInstance() to App.getInstance().getString(R.string.my_tab),
            AllRoutesFragment.newInstance() to App.getInstance().getString(R.string.all_tab)
        )
        listFragment.postValue(fragments)
    }

    fun getListFragments(): MutableLiveData<List<Pair<Fragment, String>>> {
        return listFragment
    }
}