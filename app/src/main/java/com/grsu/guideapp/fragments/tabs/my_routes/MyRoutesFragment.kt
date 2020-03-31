package com.grsu.guideapp.fragments.tabs.my_routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grsu.guideapp.R
import com.grsu.guideapp.adapters.AllRoutesListAdapter
import com.grsu.guideapp.fragments.tabs.RoutesTabFragmentDirections
import com.grsu.guideapp.utils.extensions.navigate

class MyRoutesFragment : Fragment(), (View, Int) -> Unit {

    private var routesAdapter: AllRoutesListAdapter? = null
    private val model: MyRoutesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        routesAdapter = AllRoutesListAdapter(listener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_my_routes, container, false)
        view.findViewById<RecyclerView>(R.id.rv_fragment_list_routes)?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = routesAdapter
        }

        model.getData().observe(this, Observer { list -> routesAdapter?.submitList(list) })

        return view
    }

    companion object {
        fun newInstance(): MyRoutesFragment = MyRoutesFragment()
    }

    override fun invoke(view: View, position: Int) {
        routesAdapter?.getItem(position)?.also {
            navigate(RoutesTabFragmentDirections.actionRoutesToGraphRoute(it.id.toInt(), it.name))
        }
    }
}