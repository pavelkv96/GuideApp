package com.grsu.guideapp.ui.fragments.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grsu.guideapp.BuildConfig
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.activities.SharedViewModel
import com.grsu.guideapp.ui.adapters.AboutAdapter

class AboutFragment : Fragment(), (View, Int) -> Unit {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewVersion: TextView
    private val sharedModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        recyclerView = view.findViewById(R.id.fragment_about_rv)
        textViewVersion = view.findViewById(R.id.fragment_about_version)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            sharedModel.finishApplication()
        }

        val text = getString(R.string.title_about_app, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
        textViewVersion.text = text

        recyclerView.also {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = AboutAdapter(getData(), this)
        }

        return view
    }

    override fun invoke(view: View, position: Int) {
        when (position) {
//            0 -> openURL(R.string.nav_header_subtitle)
            0 -> composeEmail()
            1 -> rateMe()
        }
    }

    private fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address_iac)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_feedback))
        }
        context?.also {
            if (intent.resolveActivity(it.packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    private fun rateMe() {
        try {
            val uri = Uri.parse("market://details?id=${context!!.packageName}")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            val uri = Uri.parse("http://play.google.com/store/apps/details?id=${context!!.packageName}")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    @Suppress("unused")
    private fun openURL(@StringRes urlId: Int) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(urlId))))
    }

    private fun getData(): List<AboutItem> {
        val items = mutableListOf<AboutItem>()
//        items.add(AboutItem(R.string.nav_header_title, R.string.action_about_openURL))
        items.add(AboutItem(R.string.title_about_contact_developer, R.string.action_about_contact_developer))
        items.add(AboutItem(R.string.title_about_rate_app, R.string.action_about_rate_app))
        return items
    }
}
