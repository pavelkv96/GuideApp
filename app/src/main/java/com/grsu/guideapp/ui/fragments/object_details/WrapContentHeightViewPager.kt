package com.grsu.guideapp.ui.fragments.object_details

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class WrapContentHeightViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasure = heightMeasureSpec
        var height = 0
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h: Int = child.measuredHeight
            if (h > height) height = h
        }
        heightMeasure = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

}