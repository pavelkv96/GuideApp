package com.grsu.guideapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    private var fragmentsAndTitles: List<Pair<Fragment, String>> = mutableListOf(),
    manager: FragmentManager
) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = fragmentsAndTitles.size

    override fun getItem(position: Int): Fragment = fragmentsAndTitles[position].first

    override fun getPageTitle(position: Int): CharSequence = fragmentsAndTitles[position].second

    fun submitList(fragments: List<Pair<Fragment, String>>) {
        fragmentsAndTitles = fragments
        notifyDataSetChanged()
    }

//    fun addFragment(fragment: Pair<Fragment, String>) = fragmentsAndTitles.add(fragment)
}