package com.vtnd.lus.shared

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import kotlin.math.max
import kotlin.math.min


class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {
        if (dependency is Snackbar.SnackbarLayout) updateSnackBar(child, dependency)
        return dependency is FrameLayout
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed < 0) showBottomNavigationView(child);
        else if (dyConsumed > 0) hideBottomNavigationView(child);
    }

    private fun hideBottomNavigationView(view: V) {
        view.animate().translationY(view.height.toFloat()).duration = 100
    }

    private fun showBottomNavigationView(view: V) {
        view.animate().translationY(0f).duration = 100
    }

    private fun updateSnackBar(child: View, snackBarLayout: Snackbar.SnackbarLayout) {
        if (snackBarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
            val params = snackBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackBarLayout.layoutParams = params
        }
    }
}

// Apply to FAB anchored to BottomNavigationView
class BottomNavigationFABBehavior(
    context: Context?,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ) = dependency is Snackbar.SnackbarLayout

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        child.translationY = 0f
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: View,
        dependency: View
    ) = updateButton(child, dependency)

    private fun updateButton(child: View, dependency: View) =
        if (dependency is Snackbar.SnackbarLayout) {
            val oldTranslation = child.translationY
            val height = dependency.height.toFloat()
            val newTranslation = dependency.translationY - height
            child.translationY = newTranslation
            oldTranslation != newTranslation
        } else false
}
