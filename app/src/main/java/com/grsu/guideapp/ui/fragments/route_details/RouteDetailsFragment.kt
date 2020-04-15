package com.grsu.guideapp.ui.fragments.route_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.grsu.guideapp.R
import com.grsu.guideapp.utils.extensions.navigate
import timber.log.Timber

class RouteDetailsFragment : Fragment(R.layout.fragment_route_preview) {

    private lateinit var args: RouteDetailsFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_route_preview, container, false)

        args = RouteDetailsFragmentArgs.fromBundle(requireArguments())
        Timber.e("id = ${args.routeId}")
        Timber.e("title = ${args.title}")

        view.findViewById<AppCompatButton>(R.id.map_zoom_out).setOnClickListener {
            navigate(RouteDetailsFragmentDirections.actionRoutesToRouteMap(args.routeId, args.title))
        }

        return view
    }
}