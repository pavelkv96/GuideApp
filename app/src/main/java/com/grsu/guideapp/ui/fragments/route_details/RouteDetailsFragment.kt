package com.grsu.guideapp.ui.fragments.route_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.grsu.guideapp.R
//TODO navigation
//import com.grsu.guideapp.RouteGraphArgs
import com.grsu.guideapp.utils.extensions.navigate
import timber.log.Timber

class RouteDetailsFragment : Fragment(R.layout.fragment_route_preview) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_route_preview, container, false)

        arguments?.apply {
            val args = RouteDetailsFragmentArgs.fromBundle(this)
            Timber.e("id = ${args.routeId}")
            Timber.e("title = ${args.title}")
        } ?: Timber.e("Empty args")

        view.findViewById<AppCompatButton>(R.id.map_zoom_out).setOnClickListener {
            navigate(RouteDetailsFragmentDirections.actionRoutesToTest())
        }

        return view
    }
}