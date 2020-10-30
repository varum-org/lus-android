package com.vtnd.lus.shared.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class SwipeLockableViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var swipeEnabled = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent) = when (swipeEnabled) {
        true -> super.onTouchEvent(event)
        false -> false
    }

    override fun onInterceptTouchEvent(event: MotionEvent) = when (swipeEnabled) {
        true -> super.onInterceptTouchEvent(event)
        false -> false
    }

    fun setSwipePagingEnabled(swipeEnabled: Boolean) {
        this.swipeEnabled = swipeEnabled
    }
}
