package com.grsu.guideapp.ui.fragments.object_details

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textview.MaterialTextView
import com.grsu.guideapp.App
import com.grsu.guideapp.R
import com.grsu.guideapp.ui.activities.MainActivity
import com.grsu.guideapp.utils.base.Result
import com.grsu.guideapp.data.local.database.vo.DetailsObjectVO
import com.grsu.guideapp.utils.extensions.*
import timber.log.Timber

class ObjectDetailsFragment : Fragment(), ((View, Int) -> Unit) {

    private lateinit var imageAdapter: ImagePagerAdapter
    private lateinit var otherAdapter: DetailsPagerAdapter
    private lateinit var imageViewPager: ViewPager
    private lateinit var otherViewPager: WrapContentHeightViewPager
    private lateinit var description: MaterialTextView
    private lateinit var likeButton: FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var model: ObjectDetailsModel
    private lateinit var args: ObjectDetailsFragmentArgs
    private lateinit var tabs: TabLayout

    private var pageOther = 0
    private var pageImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val picasso = App.getInstance().getPicasso()
        args = with(requireArguments()) { ObjectDetailsFragmentArgs.fromBundle(this) }
        val image = args.photo ?: ""

        pageOther = savedInstanceState?.getInt(key_page_other, 0) ?: 0
        pageImage = savedInstanceState?.getInt(key_page_image, 0) ?: 0

        imageAdapter = ImagePagerAdapter(listOf(DetailsObjectVO.ImagesVO(image.toMD5(), image)), this, picasso)
        otherAdapter = DetailsPagerAdapter(listener = this)

        model = ViewModelProvider(this, ModelFactory(args.objectId))[ObjectDetailsModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_object_details, container, false)
        view.apply {
            toolbar = findViewById(R.id.object_details_toolbar)
            likeButton = findViewById(R.id.fab_fragment_object_details_like)
            imageViewPager = findViewById(R.id.object_details_view_pager)
            otherViewPager = findViewById(R.id.view_pager)
            description = findViewById(R.id.mtv_fragment_object_details_description)
            tabs = findViewById(R.id.tab_layout)
        }

        (requireActivity() as? MainActivity)?.also {
            toolbar.title = args.title
            toolbar.navigationIcon?.setColorFilter(getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            toolbar.setNavigationOnClickListener { popBackStack() }

            it.getAppBarLayout().hide()
        }

        tabs.setupWithViewPager(otherViewPager)

        imageViewPager.adapter = imageAdapter
        imageViewPager.setPageTransformer(true, DepthPageTransformer())
        otherViewPager.adapter = otherAdapter

        likeButton.setOnClickListener {
            model.setLike()
            Toast.makeText(requireContext(), "Like button clicked", Toast.LENGTH_SHORT).show()
        }
        model.content().observe(this, Observer {
            if (it is Result.Success) {
                description.text = HtmlCompat.fromHtml(it.data.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        })
        model.otherContent().observe(this, Observer {
            if (it is Result.Success) {
                otherAdapter.submitList(it.data)
                otherViewPager.setCurrentItem(pageOther, true)
                it.data.forEachIndexed { i, content -> tabs.getTabAt(i)?.setIcon(content.icon) }
            }
        })
        model.listImage().observe(this, Observer {
            if (it is Result.Success) {
                imageAdapter.submitList(it.data)
                imageViewPager.setCurrentItem(pageImage, true)
            }
        })
        model.isLikedLiveDate().observe(this, Observer {
            if (it) likeButton.setImageResource(R.drawable.ic_favorite_remove)
            else likeButton.setImageResource(R.drawable.ic_favorite_add)
        })
        return view
    }

    override fun onDestroyView() {
        (requireActivity() as? MainActivity)?.apply { getAppBarLayout().show() }
        super.onDestroyView()
    }

    override fun invoke(view: View, position: Int) {
        when (view) {
            is MaterialTextView -> {
                Timber.e("Action position is $position")
            }
            is ImageView -> {
                Timber.e("Image position is $position")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(key_page_other, otherViewPager.currentItem)
        outState.putInt(key_page_image, imageViewPager.currentItem)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val key_page_other = "pager_other"
        private const val key_page_image = "pager_image"
    }
}