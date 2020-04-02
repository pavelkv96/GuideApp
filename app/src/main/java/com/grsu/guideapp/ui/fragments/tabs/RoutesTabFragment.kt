package com.grsu.guideapp.ui.fragments.tabs

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.activities.SharedViewModel
import com.grsu.guideapp.ui.adapters.PagerAdapter
import com.grsu.guideapp.utils.extensions.navigate

class RoutesTabFragment : Fragment() {

    private val model: RouteTabViewModel by viewModels()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private val sharedModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerAdapter = PagerAdapter(manager = childFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_routes_tab, container, false)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            sharedModel.finishApplication()
        }
        with(view) {
            tabLayout = findViewById(R.id.tl_fragment_routes_tab)
            viewPager = findViewById(R.id.vp_fragment_routes_tab)
            viewPager.adapter = pagerAdapter
            tabLayout.setupWithViewPager(viewPager)
        }
        model.getListFragments().observe(this, Observer { pagerAdapter.submitList(it) })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_objects, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (R.id.action_list_objects == item.itemId) navigate(R.id.action_routes_to_catalog)
        return super.onOptionsItemSelected(item)
    }
}